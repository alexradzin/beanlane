package org.beanlane;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;


/**
 * Configures annotation that should be used to extract the bean property name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BeanNameAnnotation {
    /**
     * The annotation that holds customized bean property
     */
    Class<? extends Annotation> value();

    /**
     * The name of annotation method that holds the property name.
     */
    String field() default "value";

    Class<? extends Function<String, String>> formatter() default NoOp.class;

    class NoOp implements Function<String, String> {
        @Override
        public String apply(String s) {
            return s;
        }
    }
}
