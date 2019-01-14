package org.beanlane;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.beanlane.NameExtractor.BeanNameExtractor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanLane {
    private final Map<Class, Optional<Object>> defaultValue = new HashMap<Class, Optional<Object>>() {{
        put(byte.class, Optional.of(0));
        put(short.class, Optional.of(0));
        put(int.class, Optional.of(0));
        put(long.class, Optional.of(0));
        put(char.class, Optional.of(0));
        put(float.class, Optional.of(0.0F));
        put(double.class, Optional.of(0.0));
        put(boolean.class, Optional.of(false));
        put(String.class, Optional.ofNullable(null));
    }};

    private final ThreadLocal<String> nameHolder = new ThreadLocal<>();
    private final Function<Method, String> fieldNameExtractor;

    public BeanLane() {
        this(new BeanNameExtractor());
    }

    public BeanLane(Function<Method, String> fieldNameExtractor) {
        this.fieldNameExtractor = fieldNameExtractor;
        nameHolder.remove();
    }

    @SuppressWarnings("restriction")
    public <T> T of(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setUseCache(false);
        MethodInterceptor interceptor = (obj, method, args, proxy) -> {
            nameHolder.set(nameHolder.get() == null ? fieldNameExtractor.apply(method) : String.join(".", nameHolder.get(), fieldNameExtractor.apply(method)));
            return defaultValue.computeIfAbsent(method.getReturnType(), aClass -> Optional.of(of(method.getReturnType()))).orElse(null); // orElse(null) - is it OK or should throw excption?
        };
        enhancer.setCallbackType(interceptor.getClass());
        final Class<?> proxyClass = enhancer.createClass();
        Enhancer.registerCallbacks(proxyClass, new Callback[] {interceptor});
        try {
            @SuppressWarnings("unchecked")
            T result = (T)ReflectionFactory.getReflectionFactory().newConstructorForSerialization(proxyClass, Object.class.getDeclaredConstructor()).newInstance();
            return result;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }


    public <T> String name(Supplier<T> f) {
        try {
            f.get();
            return nameHolder.get();
        } finally {
            nameHolder.remove();
        }
    }
}
