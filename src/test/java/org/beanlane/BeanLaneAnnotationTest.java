package org.beanlane;


import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanNameAnnotation(value = XmlElement.class, field = "name")
@VisibleForPackage
class BeanLaneAnnotationTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("FirstName", $(p::getFirstName));
        assertEquals("LastName", $(p::getLastName));
        assertEquals("HomeAddress", $(p::getHome));
        assertEquals("HomeAddress.ZIP", $(() -> p.getHome().getZip()));
        assertEquals("HomeAddress.City", $(() -> p.getHome().getCity()));
        assertEquals("HomeAddress.Country", $(() -> p.getHome().getCountry()));
        assertEquals("HomeAddress.Street", $(() -> p.getHome().getStreet()));
        assertEquals("HomeAddress.StreetNumber", $(() -> p.getHome().getStreetNumber()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("FirstName", name(p::getFirstName));
        assertEquals("LastName", name(p::getLastName));
        assertEquals("HomeAddress", name(p::getHome));
        assertEquals("HomeAddress.ZIP", name(() -> p.getHome().getZip()));
        assertEquals("HomeAddress.City", name(() -> p.getHome().getCity()));
        assertEquals("HomeAddress.Country", name(() -> p.getHome().getCountry()));
        assertEquals("HomeAddress.Street", name(() -> p.getHome().getStreet()));
        assertEquals("HomeAddress.StreetNumber", name(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notField() {
        Address a = $(Address.class);
        assertThrows(UnsupportedOperationException.class, a::getZipCode);
    }

}
