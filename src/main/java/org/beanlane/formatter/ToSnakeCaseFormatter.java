package org.beanlane.formatter;

import java.util.Arrays;
import java.util.function.Function;

import static java.lang.String.format;

public class ToSnakeCaseFormatter implements Function<String, String> {
    protected static final String DEFAULT_SEPARATOR = "_";
    private final String separator;
    private final boolean upperCase;
    private final String sameWrongCaseRegex;
    private final Function<String, String> fixCase;


    public ToSnakeCaseFormatter() {
        this(DEFAULT_SEPARATOR, false);
    }

    public ToSnakeCaseFormatter(String separator) {
        this(separator, false);
    }

    public ToSnakeCaseFormatter(boolean upperCase) {
        this(DEFAULT_SEPARATOR, upperCase);
    }

    public ToSnakeCaseFormatter(String separator, boolean upperCase) {
        this.separator = separator;
        this.upperCase = upperCase;
        this.sameWrongCaseRegex = upperCase ? "[a-z0-9]+" : "[A-Z0-9]+";
        this.fixCase = upperCase ? String::toUpperCase : String::toLowerCase;
    }

    @Override
    public String apply(String method) {
        String name = String.join(
                separator,
                Arrays.stream(method.split("(?=[A-Z][a-z])"))
                        .map(s -> s.matches(sameWrongCaseRegex) ? fixCase.apply(s) : (s.substring(0, 1).toLowerCase() + s.substring(1))).toArray(String[]::new));
        if (upperCase) {
            name = name.toUpperCase();
        }
        return name;
    }
}
