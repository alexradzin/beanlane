package org.beanlane;


import org.beanlane.NameExtractor.ToCapitalSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanNameAnnotation(value = XmlElement.class, field = "name", formatter = ToCapitalSnakeCaseFormatter.class)
class BeanLaneAnnotationWithCapitalSnakeFormatterTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("FIRST_NAME", $(p::getFirstName));
        assertEquals("LAST_NAME", $(p::getLastName));
        assertEquals("HOME_ADDRESS", $(p::getHome));
        assertEquals("HOME_ADDRESS.ZIP", $(() -> p.getHome().getZip()));
        assertEquals("HOME_ADDRESS.CITY", $(() -> p.getHome().getCity()));
        assertEquals("HOME_ADDRESS.COUNTRY", $(() -> p.getHome().getCountry()));
        assertEquals("HOME_ADDRESS.STREET", $(() -> p.getHome().getStreet()));
        assertEquals("HOME_ADDRESS.STREET_NUMBER", $(() -> p.getHome().getStreetNumber()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("FIRST_NAME", name(p::getFirstName));
        assertEquals("LAST_NAME", name(p::getLastName));
        assertEquals("HOME_ADDRESS", name(p::getHome));
        assertEquals("HOME_ADDRESS.ZIP", name(() -> p.getHome().getZip()));
        assertEquals("HOME_ADDRESS.CITY", name(() -> p.getHome().getCity()));
        assertEquals("HOME_ADDRESS.COUNTRY", name(() -> p.getHome().getCountry()));
        assertEquals("HOME_ADDRESS.STREET", name(() -> p.getHome().getStreet()));
        assertEquals("HOME_ADDRESS.STREET_NUMBER", name(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notField() {
        Address a = $(Address.class);
        assertThrows(IllegalArgumentException.class, a::getZipCode);
    }

    @Test
    void re() {
        System.out.println(Arrays.toString("homePage".split("(?=[A-Z][a-z])")));
        System.out.println(Arrays.toString("ZIP".split("(?=[A-Z][a-z])")));
    }

}
