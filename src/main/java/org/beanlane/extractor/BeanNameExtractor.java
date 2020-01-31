package org.beanlane.extractor;

import org.beanlane.formatter.CapitalizationFormatter;

import java.lang.reflect.Method;
import java.util.function.Function;


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

    private String stripGetterPrefix(String getter) {
        return getter.startsWith("get") ? getter.substring(3) : getter.startsWith("is") ? getter.substring(2) : getter;
    }

}
