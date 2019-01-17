package org.beanlane;


import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BeanNameAnnotation(value = XmlElement.class, field = "name")
class BeanLaneAnnotationTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyName() {
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
}
