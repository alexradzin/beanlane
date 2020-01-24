package org.beanlane;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BeanName {
    String separator() default BeanLane.DEFAULT_SEPARATOR;

    BeanNameFormatter[] formatter() default @BeanNameFormatter;
}
