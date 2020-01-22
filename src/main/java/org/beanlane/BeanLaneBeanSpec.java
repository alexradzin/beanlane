package org.beanlane;

import org.beanlane.extractor.BeanNameExtractor;

import java.util.function.Supplier;

public interface BeanLaneBeanSpec {
    BeanLane bean = new BeanLane(new BeanNameExtractor());

    default <T> T $(Class<T> clazz) {
        return bean.of(clazz);
    }

    default <T> String $(Supplier<T> f)  {
        return bean.name(f);
    }

    default <T> T wrap(Class<T> clazz) {
        return $(clazz);
    }

    default <T> String name(Supplier<T> f)  {
        return $(f);
    }
}
