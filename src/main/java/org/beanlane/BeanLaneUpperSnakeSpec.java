package org.beanlane;

import org.beanlane.extractor.BeanNameExtractor;
import org.beanlane.formatter.ToSnakeCaseFormatter;

import java.util.function.Supplier;

public interface BeanLaneUpperSnakeSpec {
    BeanLane upperSnake = new BeanLane(new BeanNameExtractor(new ToSnakeCaseFormatter(true)));

    default <T> T wrap(Class<T> clazz) {
        return ___(clazz);
    }

    default <T> String name(Supplier<T> f)  {
        return ___(f);
    }


    default <T> T ___(Class<T> clazz) {
        return upperSnake.of(clazz);
    }

    default <T> String ___(Supplier<T> f)  {
        return upperSnake.name(f);
    }
}
