package org.beanlane;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanLane {
    private static final Map<Class, Optional<Object>> defaultValue = new HashMap<>();
    static {
        defaultValue.put(byte.class, Optional.of(0));
        defaultValue.put(short.class, Optional.of(0));
        defaultValue.put(int.class, Optional.of(0));
        defaultValue.put(long.class, Optional.of(0));
        defaultValue.put(char.class, Optional.of(0));
        defaultValue.put(float.class, Optional.of(0.0F));
        defaultValue.put(double.class, Optional.of(0.0));
        defaultValue.put(boolean.class, Optional.of(false));
        defaultValue.put(String.class, Optional.ofNullable(null));
    }
    private final ThreadLocal<String> nameHolder = new ThreadLocal<>();
    private Function<Method, String> fieldNameExtractor = new Function<Method, String>() {
        @Override
        public String apply(Method method) {
            String getter = method.getName();
            String name = getter.startsWith("get") ? getter.substring(3) : getter.startsWith("is") ? getter.substring(2) : null;
            if (name == null) {
                throw new IllegalArgumentException(String.format("Invoked method %s must be a getter", getter));
            }
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
    };


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
