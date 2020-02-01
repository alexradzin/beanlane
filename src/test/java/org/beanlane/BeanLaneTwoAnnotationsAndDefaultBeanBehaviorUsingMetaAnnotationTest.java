package org.beanlane;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.beanlane.formatter.CapitalizationFormatter;
import org.beanlane.formatter.GetterFormatter;

import javax.xml.bind.annotation.XmlElement;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@BeanLaneTwoAnnotationsAndDefaultBeanBehaviorUsingMetaAnnotationTest.JsonXmlPojo
@VisibleForPackage
class BeanLaneTwoAnnotationsAndDefaultBeanBehaviorUsingMetaAnnotationTest extends BeanLaneTwoAnnotationsAndDefaultBeanBehaviorTestCase {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    @BeanPropertyExtractor(value = JsonProperty.class)
    @BeanPropertyExtractor(value = XmlElement.class, field = "name")
    @BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = GetterFormatter.class), @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false")})
    @interface JsonXmlPojo {
    }
}