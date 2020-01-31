package org.beanlane;

import org.beanlane.extractor.PropertyNameExtractor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface BeanLaneBeanSpec {
    Map<Class, BeanLane> br = new ConcurrentHashMap<>(1);
    BeanLane bean = new BeanLane(new PropertyNameExtractor());

    default <T> T wrap(Class<T> clazz) {
        Class<?> specClass = getClass();
        BeanPropertyExtractor annotation = specClass.getAnnotation(BeanPropertyExtractor.class);
        if (annotation == null) {
            return br.computeIfAbsent(getClass(), x -> bean).of(clazz);
        }

        String separator = BeanPropertyExtractor.DEFAULT_SEPARATOR.equals(annotation.separator()) ? "." : annotation.separator();
        return BeanLane.create(
                specClass,
                annotation.formatter(),
                br,
                separator,
                PropertyNameExtractor::new).of(clazz);
    }

    default <T> String $(Supplier<T> f)  {
        return br.get(getClass()).name(f);
    }

    default <T> T $(Class<T> clazz) {
        return wrap(clazz);
    }

    default <T> String name(Supplier<T> f)  {
        return $(f);
    }
}
