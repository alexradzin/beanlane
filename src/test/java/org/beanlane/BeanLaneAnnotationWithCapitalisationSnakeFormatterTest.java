package org.beanlane;


import org.beanlane.formatter.CapitalizationFormatter;
import org.beanlane.formatter.CaseFormatter;
import org.beanlane.formatter.ToSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanNameAnnotation(
        value = XmlElement.class,
        field = "name",
        formatter = {
                @BeanNameFormatter(value = ToSnakeCaseFormatter.class, args = "#"),
                @BeanNameFormatter(value = CaseFormatter.class, args = "LOWER"),
                @BeanNameFormatter(value = CapitalizationFormatter.class, args = "true")
        })
@VisibleForPackage
class BeanLaneAnnotationWithCapitalisationSnakeFormatterTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("First#name", $(p::getFirstName));
        assertEquals("Last#name", $(p::getLastName));
        assertEquals("Home#address", $(p::getHome));
        assertEquals("Home#address.Zip", $(() -> p.getHome().getZip()));
        assertEquals("Home#address.City", $(() -> p.getHome().getCity()));
        assertEquals("Home#address.Country", $(() -> p.getHome().getCountry()));
        assertEquals("Home#address.Street", $(() -> p.getHome().getStreet()));
        assertEquals("Home#address.Street#number", $(() -> p.getHome().getStreetNumber()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("First#name", name(p::getFirstName));
        assertEquals("Last#name", name(p::getLastName));
        assertEquals("Home#address", name(p::getHome));
        assertEquals("Home#address.Zip", name(() -> p.getHome().getZip()));
        assertEquals("Home#address.City", name(() -> p.getHome().getCity()));
        assertEquals("Home#address.Country", name(() -> p.getHome().getCountry()));
        assertEquals("Home#address.Street", name(() -> p.getHome().getStreet()));
        assertEquals("Home#address.Street#number", name(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notField() {
        Address a = $(Address.class);
        assertThrows(UnsupportedOperationException.class, a::getZipCode);
    }
}
