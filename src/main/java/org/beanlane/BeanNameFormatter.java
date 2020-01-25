package org.beanlane;

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
public @interface BeanNameFormatter {
    Class<? extends Function<String, String>> value() default NoOp.class;
    Class<?>[] paramTypes() default {BeanNameFormatter.class};
    String[] args() default {};


    class NoOp implements Function<String, String> {
        @Override
        public String apply(String s) {
            return s;
        }
    }
}
