package org.beanlane.formatter;

import java.util.function.Function;

public class GetterFormatter implements Function<String, String> {
    @Override
    public String apply(String name) {
        return name.startsWith("get") ? name.substring(3) : name.startsWith("is") ? name.substring(2) : name;
    }
}
