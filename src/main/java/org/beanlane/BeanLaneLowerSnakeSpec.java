package org.beanlane;

import org.beanlane.extractor.PropertyNameExtractor;
import org.beanlane.formatter.GetterFormatter;
import org.beanlane.formatter.ToSnakeCaseFormatter;

import java.util.Arrays;
import java.util.function.Supplier;

public interface BeanLaneLowerSnakeSpec {
    BeanLane snake = new BeanLane(new PropertyNameExtractor(new ChainedFunction<>(Arrays.asList(new GetterFormatter(), new ToSnakeCaseFormatter(false)))));

    default <T> T wrap(Class<T> clazz) {
        return __(clazz);
    }

    default <T> T __(Class<T> clazz) {
        return snake.of(clazz);
    }


    default <T> String name(Supplier<T> f)  {
        return __(f);
    }

    default <T> String __(Supplier<T> f)  {
        return snake.name(f);
    }

}
