package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@VisibleForPackage
class BeanLaneUpperSnakeSpecTest implements BeanLaneUpperSnakeSpec {
    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("FIRST_NAME", name(p::getFirstName));
        assertEquals("LAST_NAME", name(p::getLastName));
        assertEquals("HOME", name(p::getHome));
        assertEquals("HOME.ZIP", name(() -> p.getHome().getZip()));
        assertEquals("HOME.CITY", name(() -> p.getHome().getCity()));
        assertEquals("HOME.COUNTRY", name(() -> p.getHome().getCountry()));
        assertEquals("HOME.STREET", name(() -> p.getHome().getStreet()));
        assertEquals("HOME.STREET_NUMBER", name(() -> p.getHome().getStreetNumber()));
    }
}