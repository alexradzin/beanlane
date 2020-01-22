package org.beanlane.formatter;

import java.util.function.Function;

public class CaseFormatter implements Function<String, String> {
    private final Case toCase;

    public CaseFormatter(Case toCase) {
        this.toCase = toCase;
    }

    public enum Case implements Function<String, String> {
        UPPER {
            @Override
            public String apply(String s) {
                return s == null ? null : s.toUpperCase();
            }
        },
        LOWER {
            @Override
            public String apply(String s) {
                return s == null ? null : s.toLowerCase();
            }
        },
        ;
    }


    @Override
    public String apply(String s) {
        return toCase.apply(s);
    }
}
