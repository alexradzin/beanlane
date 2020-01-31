package org.beanlane.extractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Function;

import static java.lang.String.format;

public class PropertyNameAnnotationExtractor implements Function<Method, String> {
    private final Class<? extends Annotation> annotationClass;
    private final String field;
    private final Function<String, String> formatter;
    private final PropertyNameExtractor nameExtractor = new PropertyNameExtractor();

    public PropertyNameAnnotationExtractor(Class<? extends Annotation> annotationClass, String field) {
        this(annotationClass, field, s -> s);
    }

    public PropertyNameAnnotationExtractor(Class<? extends Annotation> annotationClass, String field, Function<String, String> formatter) {
        this.annotationClass = annotationClass;
        this.field = field;
        this.formatter = formatter;
    }

    @Override
    public String apply(Method method) {
        Annotation annotation = method.getAnnotation(annotationClass);
        String fieldName = nameExtractor.apply(method);
        if (annotation == null) {
            for (Class c = method.getDeclaringClass(); c != null; c = c.getSuperclass()) {
                try {
                    annotation = c.getDeclaredField(fieldName).getAnnotation(annotationClass);
                } catch (NoSuchFieldException e) {
                    // Nothing to do. Field is not found, the exception will be thrown in the next line.
                }
            }
        }

        if (annotation == null) {
            throw new UnsupportedOperationException(format(
                    "Neither getter %s nor corresponding field %s is not marked with annotation %s",
                    method.getName(), fieldName, annotationClass.getName()));
        }

        try {
            return formatter.apply((String)annotationClass.getMethod(field).invoke(annotation));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot extract name value from " + annotationClass + "." + field, e);
        }
    }
}
