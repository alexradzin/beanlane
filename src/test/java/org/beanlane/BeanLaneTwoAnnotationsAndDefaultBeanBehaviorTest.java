package org.beanlane;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.beanlane.formatter.CapitalizationFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BeanPropertyExtractor(value = JsonProperty.class)
@BeanPropertyExtractor(value = XmlElement.class, field = "name")
@BeanPropertyExtractor(formatter = @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false"))
@VisibleForPackage
class BeanLaneTwoAnnotationsAndDefaultBeanBehaviorTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("first-name", $(p::getFirstName));
        assertEquals("last-name", $(p::getLastName));
        assertEquals("HomeAddress", $(p::getHome));
        assertEquals("HomeAddress.ZIP", $(() -> p.getHome().getZip()));
        assertEquals("HomeAddress.town", $(() -> p.getHome().getCity()));
        assertEquals("HomeAddress.state", $(() -> p.getHome().getCountry()));
        assertEquals("HomeAddress.street", $(() -> p.getHome().getStreet()));
        assertEquals("HomeAddress.number", $(() -> p.getHome().getStreetNumber()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("first-name", name(p::getFirstName));
        assertEquals("last-name", name(p::getLastName));
        assertEquals("HomeAddress", name(p::getHome));
        assertEquals("HomeAddress.ZIP", name(() -> p.getHome().getZip()));
        assertEquals("HomeAddress.town", name(() -> p.getHome().getCity()));
        assertEquals("HomeAddress.state", name(() -> p.getHome().getCountry()));
        assertEquals("HomeAddress.street", name(() -> p.getHome().getStreet()));
        assertEquals("HomeAddress.number", name(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notAnnotatedField() {
        Address a = $(Address.class);
        assertEquals("zipCode", name(a::getZipCode));
    }
}