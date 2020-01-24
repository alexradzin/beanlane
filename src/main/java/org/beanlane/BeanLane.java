package org.beanlane;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.beanlane.extractor.BeanNameExtractor;
import org.beanlane.formatter.FormatterFactory;

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
import java.util.stream.Collectors;

public class BeanLane {
    @VisibleForPackage
    static final String DEFAULT_SEPARATOR = ".";
    private final Map<Class, Optional<Object>> defaultValue = new HashMap<>();

    private final ThreadLocal<String> nameHolder = new ThreadLocal<>();
    private final Object lock = new Object();
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

        defaultValue.put(byte.class, Optional.of(0));
        defaultValue.put(short.class, Optional.of(0));
        defaultValue.put(int.class, Optional.of(0));
        defaultValue.put(long.class, Optional.of(0));
        defaultValue.put(char.class, Optional.of(0));
        defaultValue.put(float.class, Optional.of(0.0F));
        defaultValue.put(double.class, Optional.of(0.0));
        defaultValue.put(boolean.class, Optional.of(false));
        defaultValue.put(String.class, Optional.empty());
    }

    public <T> T of(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setUseCache(false);
        MethodInterceptor interceptor = (obj, method, args, proxy) -> {
            nameHolder.set(nameHolder.get() == null ? fieldNameExtractor.apply(method) : String.join(separator, nameHolder.get(), fieldNameExtractor.apply(method)));
            return defaultValue.computeIfAbsent(method.getReturnType(), aClass -> Optional.of(of(method.getReturnType()))).orElse(null); // orElse(null) - is it OK or should throw exception?
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
        synchronized (lock) {
            try {
                f.get();
                return nameHolder.get();
            } finally {
                nameHolder.remove();
            }
        }
    }

    @VisibleForPackage static BeanLane create(Class<?> specClass, BeanNameFormatter[] formatterConfigurations, Map<Class, BeanLane> br, String laneSeparator, Function<Function<String, String>, Function<Method, String>> nameExtractorSupplier) {
        Function<String, String> formatter = new CompositeFunction<>(
                Arrays.stream(formatterConfigurations)
                        .map(conf -> FormatterFactory.create(conf.value(), conf.args()))
                        .collect(Collectors.toList()));
        return br.computeIfAbsent(
                specClass,
                s -> new BeanLane(laneSeparator, nameExtractorSupplier.apply(formatter)));

    }
}
