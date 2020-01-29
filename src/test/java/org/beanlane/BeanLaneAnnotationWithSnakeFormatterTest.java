package org.beanlane;


import org.beanlane.formatter.ToSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanNameAnnotation(value = XmlElement.class, field = "name", formatter = @BeanNameFormatter(ToSnakeCaseFormatter.class))
class BeanLaneAnnotationWithSnakeFormatterTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("first_name", $(p::getFirstName));
        assertEquals("last_name", $(p::getLastName));
        assertEquals("home_address", $(p::getHome));
        assertEquals("home_address.zip", $(() -> p.getHome().getZip()));
        assertEquals("home_address.city", $(() -> p.getHome().getCity()));
        assertEquals("home_address.country", $(() -> p.getHome().getCountry()));
        assertEquals("home_address.street", $(() -> p.getHome().getStreet()));
        assertEquals("home_address.street_number", $(() -> p.getHome().getStreetNumber()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("first_name", name(p::getFirstName));
        assertEquals("last_name", name(p::getLastName));
        assertEquals("home_address", name(p::getHome));
        assertEquals("home_address.zip", name(() -> p.getHome().getZip()));
        assertEquals("home_address.city", name(() -> p.getHome().getCity()));
        assertEquals("home_address.country", name(() -> p.getHome().getCountry()));
        assertEquals("home_address.street", name(() -> p.getHome().getStreet()));
        assertEquals("home_address.street_number", name(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notField() {
        Address a = $(Address.class);
        assertThrows(UnsupportedOperationException.class, a::getZipCode);
    }
}
