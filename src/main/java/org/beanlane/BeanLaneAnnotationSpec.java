package org.beanlane;

import org.beanlane.extractor.BeanNameAnnotationExtractor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.lang.String.format;

public interface BeanLaneAnnotationSpec {
    Map<Class, BeanLane> br = new ConcurrentHashMap<>(1);

    default <T> T $(Class<T> clazz) {
        return wrap(clazz);
    }

    default <T> T wrap(Class<T> clazz) {
        Class<?> specClass = getClass();
        BeanNameAnnotation annotation = specClass.getAnnotation(BeanNameAnnotation.class);
        if (annotation == null) {
            throw new IllegalArgumentException(format("Class %s is not marked with annotation %s", getClass().getName(), BeanNameAnnotation.class.getName()));
        }

        return BeanLane.create(specClass, annotation.formatter(), br, annotation.separator(), formatter -> new BeanNameAnnotationExtractor(annotation.value(), annotation.field(), formatter)).of(clazz);
    }

    default <T> String name(Supplier<T> f)  {
        return $(f);
    }

    default <T> String $(Supplier<T> f)  {
        return br.get(getClass()).name(f);
    }
}
