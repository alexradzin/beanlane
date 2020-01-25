package org.beanlane;


import org.beanlane.formatter.RegexFormatter;
import org.beanlane.formatter.ToSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanNameAnnotation(
        value = XmlElement.class, field = "name",
        formatter = {
                @BeanNameFormatter(value = RegexFormatter.class, args = {"([A-Z][a-z])", "g", "_$1"}),
                @BeanNameFormatter(value = RegexFormatter.class, args = {"^_", "", ""}),
        })
class BeanLaneAnnotationWithRegexFormatterTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("First_Name", $(p::getFirstName));
        assertEquals("Last_Name", $(p::getLastName));
        assertEquals("Home_Address", $(p::getHome));
        assertEquals("Home_Address.ZIP", $(() -> p.getHome().getZip()));
        assertEquals("Home_Address.City", $(() -> p.getHome().getCity()));
        assertEquals("Home_Address.Country", $(() -> p.getHome().getCountry()));
        assertEquals("Home_Address.Street", $(() -> p.getHome().getStreet()));
        assertEquals("Home_Address.Street_Number", $(() -> p.getHome().getStreetNumber()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("First_Name", name(p::getFirstName));
        assertEquals("Last_Name", name(p::getLastName));
        assertEquals("Home_Address", name(p::getHome));
        assertEquals("Home_Address.ZIP", name(() -> p.getHome().getZip()));
        assertEquals("Home_Address.City", name(() -> p.getHome().getCity()));
        assertEquals("Home_Address.Country", name(() -> p.getHome().getCountry()));
        assertEquals("Home_Address.Street", name(() -> p.getHome().getStreet()));
        assertEquals("Home_Address.Street_Number", name(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notField() {
        Address a = $(Address.class);
        assertThrows(IllegalArgumentException.class, a::getZipCode);
    }
}
