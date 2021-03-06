package org.beanlane;

import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BeanPropertyExtractor(value = XmlElement.class, field = "somethingWrong")
@VisibleForPackage
class BeanLaneWrongFormatterAnnotationTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyWrongName() {
        Person p = $(Person.class);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, p::getFirstName);
        assertTrue(e.getMessage().startsWith("Cannot extract name value from"));
    }
}
