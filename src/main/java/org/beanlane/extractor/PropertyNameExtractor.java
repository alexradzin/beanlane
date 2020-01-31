package org.beanlane.extractor;

import org.beanlane.ChainedFunction;
import org.beanlane.formatter.CapitalizationFormatter;
import org.beanlane.formatter.GetterFormatter;

import java.lang.reflect.Method;
import java.util.function.Function;

import static java.util.Arrays.asList;


public class PropertyNameExtractor implements Function<Method, String> {
    private static final Function<String, String> stripGetterPrefix = new GetterFormatter();
    private static final Function<String, String> toCamelCase = new CapitalizationFormatter(false);
    private static final Function<String, String> beanProperty = new ChainedFunction<>(asList(stripGetterPrefix, toCamelCase));
    private final Function<String, String> formatter;

    public PropertyNameExtractor() {
        this(beanProperty);
    }

    public PropertyNameExtractor(Function<String, String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String apply(Method method) {
        return formatter.apply(method.getName());
    }
}
