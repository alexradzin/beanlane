package org.beanlane;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Configures annotation that should be used to extract the bean property name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BeanNameAnnotation {
    String separator() default BeanLane.DEFAULT_SEPARATOR;

    /**
     * The annotation that holds customized bean property
     */
    Class<? extends Annotation> value();

    /**
     * The name of annotation method that holds the property name.
     */
    String field() default "value";

    BeanNameFormatter[] formatter() default @BeanNameFormatter;
}
