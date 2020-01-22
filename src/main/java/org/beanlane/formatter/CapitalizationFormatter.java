package org.beanlane.formatter;

import java.util.function.Function;

public class CapitalizationFormatter implements Function<String, String> {
    private final Function<String, String> firstLetterFormatter;

    public CapitalizationFormatter(boolean upper) {
        this.firstLetterFormatter = upper ? String::toUpperCase : String::toLowerCase;
    }

    @Override
    public String apply(String s) {
        return firstLetterFormatter.apply(s.substring(0, 1)) + s.substring(1);
    }
}
