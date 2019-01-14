package org.beanlane;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NameExtractor {
    static String stripGetterPrefix(Method method) {
        String getter = method.getName();
        String name = getter.startsWith("get") ? getter.substring(3) : getter.startsWith("is") ? getter.substring(2) : null;
        if (name == null) {
            throw new IllegalArgumentException(String.format("Invoked method %s must be a getter", getter));
        }
        return name;
    }

    static class BeanNameExtractor implements Function<Method, String> {
        @Override
        public String apply(Method method) {
            String name = stripGetterPrefix(method);
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
    }


    public static class SnakeNameExtractor implements Function<Method, String> {
        private final String separator;

        public SnakeNameExtractor() {
            this("_");
        }

        public SnakeNameExtractor(String separator) {
            this.separator = separator;
        }

        @Override
        public String apply(Method method) {
            return String.join(separator, Arrays.stream(stripGetterPrefix(method).split("(?=[A-Z])")).map(s -> (s.substring(0, 1).toLowerCase() + s.substring(1))).toArray(String[]::new));
        }
    }
}
