package org.beanlane;

import org.beanlane.extractor.BeanNameExtractor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface BeanLaneBeanSpec {
    Map<Class, BeanLane> br = new ConcurrentHashMap<>(1);
    BeanLane bean = new BeanLane(new BeanNameExtractor());

    default <T> T wrap(Class<T> clazz) {
        Class<?> specClass = getClass();
        BeanName annotation = specClass.getAnnotation(BeanName.class);
        if (annotation == null) {
            return br.computeIfAbsent(getClass(), x -> bean).of(clazz);
        }

        return BeanLane.create(
                specClass,
                annotation.formatter(),
                br,
                annotation.separator(),
                formatter -> new BeanNameExtractor(formatter, annotation.strict())).of(clazz);
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
