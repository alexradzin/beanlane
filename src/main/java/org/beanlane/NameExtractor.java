package org.beanlane;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

import static java.lang.String.format;

@SuppressWarnings("WeakerAccess")
public class NameExtractor {
    private static String stripGetterPrefix(Method method) {
        String getter = method.getName();
        String name = getter.startsWith("get") ? getter.substring(3) : getter.startsWith("is") ? getter.substring(2) : null;
        if (name == null) {
            throw new IllegalArgumentException(format("Invoked method %s must be a getter", getter));
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
        private static final String DEFAULT_SEPARATOR = "_";
        private final String separator;
        private final boolean upperCase;

        public SnakeNameExtractor() {
            this(DEFAULT_SEPARATOR);
        }

        public SnakeNameExtractor(String separator) {
            this(separator, false);
        }

        public SnakeNameExtractor(boolean upperCase) {
            this(DEFAULT_SEPARATOR, upperCase);
        }

        public SnakeNameExtractor(String separator, boolean upperCase) {
            this.separator = separator;
            this.upperCase = upperCase;
        }

        @Override
        public String apply(Method method) {
            String name = String.join(separator, Arrays.stream(stripGetterPrefix(method).split("(?=[A-Z])")).map(s -> (s.substring(0, 1).toLowerCase() + s.substring(1))).toArray(String[]::new));
            if (upperCase) {
                name = name.toUpperCase();
            }
            return name;
        }
    }

    static class BeanNameAnnotationExtractor implements Function<Method, String> {
        private final Class<? extends Annotation> annotationClass;
        private final String field;

        public BeanNameAnnotationExtractor(Class<? extends Annotation> annotationClass, String field) {
            this.annotationClass = annotationClass;
            this.field = field;
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
                return (String)annotationClass.getMethod(field).invoke(annotation);
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Cannot extract name value from " + annotationClass + "." + field, e);
            }
        }
    }
}
