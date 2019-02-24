package org.beanlane;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.beanlane.NameExtractor.BeanNameExtractor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanLane {
    private static final String DEFAULT_SEPARATOR = ".";
    private final Map<Class, Optional<Object>> defaultValue = new HashMap<Class, Optional<Object>>() {{
        put(byte.class, Optional.of(0));
        put(short.class, Optional.of(0));
        put(int.class, Optional.of(0));
        put(long.class, Optional.of(0));
        put(char.class, Optional.of(0));
        put(float.class, Optional.of(0.0F));
        put(double.class, Optional.of(0.0));
        put(boolean.class, Optional.of(false));
        put(String.class, Optional.empty());
    }};

    private final ThreadLocal<String> nameHolder = new ThreadLocal<>();
    private final String separator;
    private final Function<Method, String> fieldNameExtractor;


    public BeanLane() {
        this(DEFAULT_SEPARATOR);
    }

    public BeanLane(String separator) {
        this(separator, new BeanNameExtractor());
    }

    public BeanLane(Function<Method, String> fieldNameExtractor) {
        this(DEFAULT_SEPARATOR, fieldNameExtractor);
    }

    public BeanLane(String separator, Function<Method, String> fieldNameExtractor) {
        this.separator = separator;
        this.fieldNameExtractor = fieldNameExtractor;
        nameHolder.remove();
    }

    public <T> T of(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setUseCache(false);
        MethodInterceptor interceptor = (obj, method, args, proxy) -> {
            nameHolder.set(nameHolder.get() == null ? fieldNameExtractor.apply(method) : String.join(separator, nameHolder.get(), fieldNameExtractor.apply(method)));
            return defaultValue.computeIfAbsent(method.getReturnType(), aClass -> Optional.of(of(method.getReturnType()))).orElse(null); // orElse(null) - is it OK or should throw excption?
        };
        enhancer.setCallbackType(interceptor.getClass());
        final Class<?> proxyClass = enhancer.createClass();
        Enhancer.registerCallbacks(proxyClass, new Callback[] {interceptor});

        @SuppressWarnings("unchecked")
        T result = (T)Arrays.stream(proxyClass.getDeclaredConstructors()).sorted(Comparator.comparingInt(Constructor::getParameterCount))
                .map(this::newInstance)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodError("Cannot find constructor to create instance of " + clazz.getName()));
        return result;
    }

    private <T> T newInstance(Constructor<T> constructor) {
        constructor.setAccessible(true);
        try {
            return constructor
                    .newInstance(Arrays.stream(constructor.getParameterTypes()).map(p -> defaultValue.getOrDefault(p, Optional.empty()))
                    .map(p -> p.orElse(null)).toArray());
        } catch (ReflectiveOperationException e) {
            return null;
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
