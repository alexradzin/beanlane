package org.beanlane;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.beanlane.formatter.CapitalizationFormatter;
import org.beanlane.formatter.GetterFormatter;

import javax.xml.bind.annotation.XmlElement;


@BeanPropertyExtractor(value = JsonProperty.class)
@BeanPropertyExtractor(value = XmlElement.class, field = "name")
@BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = GetterFormatter.class), @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false")})
@VisibleForPackage
class BeanLaneTwoAnnotationsAndDefaultBeanBehaviorTest extends BeanLaneTwoAnnotationsAndDefaultBeanBehaviorTestCase {
}