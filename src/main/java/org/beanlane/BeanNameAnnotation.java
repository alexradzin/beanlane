package org.beanlane;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Configures annotation that should be used to extract the bean property name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(BeanNameAnnotations.class)
@Documented
public @interface BeanNameAnnotation {
    String separator() default DEFAULT_SEPARATOR;

    /**
     * The annotation that holds customized bean property
     */
    Class<? extends Annotation> value() default BeanNameAnnotation.class;

    /**
     * The name of annotation method that holds the property name.
     */
    String field() default "value";

    BeanNameFormatter[] formatter() default @BeanNameFormatter;

    String DEFAULT_SEPARATOR = "$$DEFAULT_SEPARATOR$$";

    // TODO: remove; replace by method name regex
    boolean strict() default true;
}
