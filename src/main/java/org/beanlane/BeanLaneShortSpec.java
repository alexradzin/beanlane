package org.beanlane;

import org.beanlane.extractor.BeanNameExtractor;
import org.beanlane.formatter.ToSnakeCaseFormatter;

import java.util.function.Supplier;

public interface BeanLaneShortSpec {
    BeanLane bean = new BeanLane(new BeanNameExtractor());
    BeanLane snake = new BeanLane(new BeanNameExtractor(new ToSnakeCaseFormatter()));
    BeanLane upperSnake = new BeanLane(new BeanNameExtractor(new ToSnakeCaseFormatter(true)));

    default <T> T $(Class<T> clazz) {
        return bean.of(clazz);
    }

    default <T> String $(Supplier<T> f)  {
        return bean.name(f);
    }


    default <T> T __(Class<T> clazz) {
        return snake.of(clazz);
    }


    default <T> String __(Supplier<T> f)  {
        return snake.name(f);
    }


    default <T> T ___(Class<T> clazz) {
        return upperSnake.of(clazz);
    }

    default <T> String ___(Supplier<T> f)  {
        return upperSnake.name(f);
    }
}
