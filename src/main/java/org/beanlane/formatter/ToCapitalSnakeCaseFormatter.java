package org.beanlane.formatter;

public class ToCapitalSnakeCaseFormatter extends ToSnakeCaseFormatter {
    public ToCapitalSnakeCaseFormatter() {
        super(DEFAULT_SEPARATOR, true);
    }

    public ToCapitalSnakeCaseFormatter(String separator) {
        super(separator, true);
    }
}
