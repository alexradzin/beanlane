package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanLaneLowerSnakeSpecTest implements BeanLaneLowerSnakeSpec {
    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("first_name", name(p::getFirstName));
        assertEquals("last_name", name(p::getLastName));
        assertEquals("home", name(p::getHome));
        assertEquals("home.zip", name(() -> p.getHome().getZip()));
        assertEquals("home.city", name(() -> p.getHome().getCity()));
        assertEquals("home.country", name(() -> p.getHome().getCountry()));
        assertEquals("home.street", name(() -> p.getHome().getStreet()));
        assertEquals("home.street_number", name(() -> p.getHome().getStreetNumber()));
    }
}