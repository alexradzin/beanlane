package org.beanlane;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BeanNameAnnotations {
    BeanNameAnnotation[] value();
    String separator() default BeanLane.DEFAULT_SEPARATOR;
}
