package org.beanlane;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.lang.String.format;

@SuppressWarnings("WeakerAccess")
public class NameExtractor {
    private static final Map<Class<? extends Function<String, String>>, Function<String, String>> formatters = new ConcurrentHashMap<>();


    public static Function<String, String> create(Class<? extends Function<String, String>> clazz) {
        return formatters.computeIfAbsent(clazz, (c) -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }


    private static String stripGetterPrefix(String getter) {
        String name = getter.startsWith("get") ? getter.substring(3) : getter.startsWith("is") ? getter.substring(2) : null;
        if (name == null) {
            throw new IllegalArgumentException(format("Invoked method %s must be a getter", getter));
        }
        return name;
    }

    private static final Function<String, String> toCamelCase = new CapitalizationFormatter(false);

    private static final Function<String, String> toPascalCase = name -> name.substring(0, 1).toUpperCase() + name.substring(1);

    public static class CapitalizationFormatter implements Function<String, String> {
        private final Function<String, String> firstLetterFormatter;

        public CapitalizationFormatter(boolean upper) {
            this.firstLetterFormatter = upper ? String::toUpperCase : String::toLowerCase;
        }

        @Override
        public String apply(String s) {
            return firstLetterFormatter.apply(s.substring(0, 1)) + s.substring(1);
        }
    }


    static class BeanNameExtractor implements Function<Method, String> {
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
    }


    public static class ToSnakeCaseFormatter implements Function<String, String> {
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

    public static class ToCapitalSnakeCaseFormatter extends ToSnakeCaseFormatter {
        public ToCapitalSnakeCaseFormatter() {
            super(DEFAULT_SEPARATOR, true);
        }

        public ToCapitalSnakeCaseFormatter(String separator) {
            super(separator, true);
        }
    }

    static class BeanNameAnnotationExtractor implements Function<Method, String> {
        private final Class<? extends Annotation> annotationClass;
        private final String field;
        private final Function<String, String> formatter;

        public BeanNameAnnotationExtractor(Class<? extends Annotation> annotationClass, String field) {
            this(annotationClass, field, s -> s);
        }

        public BeanNameAnnotationExtractor(Class<? extends Annotation> annotationClass, String field, Function<String, String> formatter) {
            this.annotationClass = annotationClass;
            this.field = field;
            this.formatter = formatter;
        }

        @Override
        public String apply(Method method) {
            Annotation annotation = method.getAnnotation(annotationClass);
            String fieldName = new BeanNameExtractor().apply(method);
            if (annotation == null) {
                try {
                    annotation = method.getDeclaringClass().getDeclaredField(fieldName).getAnnotation(annotationClass);
                } catch (NoSuchFieldException e) {
                    // Nothing to do. Field is not found, the exception will be thrown in the next line.
                }
            }

            if (annotation == null) {
                throw new IllegalArgumentException(format(
                        "Either getter %s or corresponding field %s are not marked with annotation %s",
                        method.getName(), fieldName, annotationClass.getName()));
            }

            try {
                return formatter.apply((String)annotationClass.getMethod(field).invoke(annotation));
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Cannot extract name value from " + annotationClass + "." + field, e);
            }
        }
    }
}
