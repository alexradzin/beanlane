package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanLaneSpecTest implements BeanLaneShortSpec {
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
    }


    @Test
    void snakePropertyName() {
        Person p = __(Person.class);
        assertEquals("first_name", __(p::getFirstName));
        assertEquals("last_name", __(p::getLastName));
        assertEquals("home", __(p::getHome));
        assertEquals("home.zip", __(() -> p.getHome().getZip()));
        assertEquals("home.city", __(() -> p.getHome().getCity()));
        assertEquals("home.country", __(() -> p.getHome().getCountry()));
        assertEquals("home.street", __(() -> p.getHome().getStreet()));
        assertEquals("home.street_number", __(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void upperCaseSnakePropertyName() {
        Person p = ___(Person.class);
        assertEquals("FIRST_NAME", ___(p::getFirstName));
        assertEquals("LAST_NAME", ___(p::getLastName));
        assertEquals("HOME", ___(p::getHome));
        assertEquals("HOME.ZIP", ___(() -> p.getHome().getZip()));
        assertEquals("HOME.CITY", ___(() -> p.getHome().getCity()));
        assertEquals("HOME.COUNTRY", ___(() -> p.getHome().getCountry()));
        assertEquals("HOME.STREET", ___(() -> p.getHome().getStreet()));
        assertEquals("HOME.STREET_NUMBER", ___(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void notGetter() {
        Address a = $(Address.class);
        assertThrows(IllegalArgumentException.class, a::zipCode);
    }

    @Test
    void constructorThrowsException() {
        assertThrows(NoSuchMethodError.class, () -> $(Word.class));
    }
}