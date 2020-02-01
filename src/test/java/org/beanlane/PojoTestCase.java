package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@VisibleForPackage
abstract class PojoTestCase implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyName() {
        Person p = $(Person.class);
        assertEquals("firstName", $(p::getFirstName));
        assertEquals("lastName", $(p::getLastName));
        assertEquals("home", $(p::getHome));
        assertEquals("home.zip", $(() -> p.getHome().getZip()));
        assertEquals("home.city", $(() -> p.getHome().getCity()));
        assertEquals("home.country", $(() -> p.getHome().getCountry()));
        assertEquals("home.street", $(() -> p.getHome().getStreet()));
        assertEquals("home.streetNumber", $(() -> p.getHome().getStreetNumber()));
        assertEquals("home.zipCode", $(() -> p.getHome().zipCode()));
    }
}
