package org.beanlane;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.lang.String.format;

public interface BeanLaneAnnotationSpec {
    Map<Class, BeanLane> br = new ConcurrentHashMap<>(1);

    default <T> T wrap(Class<T> clazz) {
        return $(clazz);
    }

    default <T> T $(Class<T> clazz) {
        BeanNameAnnotation annotation = getClass().getAnnotation(BeanNameAnnotation.class);
        if (annotation == null) {
            throw new IllegalArgumentException(format("Class %s is not marked with annotation %s", getClass().getName(), BeanNameAnnotation.class.getName()));
        }
        return br.computeIfAbsent(getClass(), s -> new BeanLane(new NameExtractor.BeanNameAnnotationExtractor(annotation.value(), annotation.field()))).of(clazz);
    }

    default <T> String name(Supplier<T> f)  {
        return $(f);
    }

    default <T> String $(Supplier<T> f)  {
        return br.get(getClass()).name(f);
    }
}
