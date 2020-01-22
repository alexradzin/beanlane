package org.beanlane.extractor;

import org.beanlane.formatter.CapitalizationFormatter;

import java.lang.reflect.Method;
import java.util.function.Function;

import static java.lang.String.format;

public class BeanNameExtractor implements Function<Method, String> {
    private static final Function<String, String> toCamelCase = new CapitalizationFormatter(false);
    private final Function<String, String> formatter;

    public BeanNameExtractor() {
        this(toCamelCase);
    }

    public BeanNameExtractor(Function<String, String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String apply(Method method) {
        return formatter.apply(stripGetterPrefix(method.getName()));
    }

    private static String stripGetterPrefix(String getter) {
        String name = getter.startsWith("get") ? getter.substring(3) : getter.startsWith("is") ? getter.substring(2) : null;
        if (name == null) {
            throw new IllegalArgumentException(format("Invoked method %s must be a getter", getter));
        }
        return name;
    }

}
