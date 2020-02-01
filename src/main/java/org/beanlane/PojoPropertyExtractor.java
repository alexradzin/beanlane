package org.beanlane;

import org.beanlane.formatter.CapitalizationFormatter;
import org.beanlane.formatter.GetterFormatter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = GetterFormatter.class), @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false")})
public @interface PojoPropertyExtractor {
}
