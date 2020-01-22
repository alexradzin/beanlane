package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@VisibleForPackage
class BeanLaneLongSpecTest extends BeanLaneShortSpecTest implements BeanLaneLongSpec {
    @Test
    void beanPropertyName() {
        Person p = bean(Person.class);
        assertEquals("firstName", bean(p::getFirstName));
        assertEquals("lastName", bean(p::getLastName));
        assertEquals("home", bean(p::getHome));
        assertEquals("home.zip", bean(() -> p.getHome().getZip()));
        assertEquals("home.city", bean(() -> p.getHome().getCity()));
        assertEquals("home.country", bean(() -> p.getHome().getCountry()));
        assertEquals("home.street", bean(() -> p.getHome().getStreet()));
        assertEquals("home.streetNumber", bean(() -> p.getHome().getStreetNumber()));
    }



    @Test
    void upperCaseSnakePropertyName() {
        Person p = usnake(Person.class);
        assertEquals("FIRST_NAME", usnake(p::getFirstName));
        assertEquals("LAST_NAME", usnake(p::getLastName));
        assertEquals("HOME", usnake(p::getHome));
        assertEquals("HOME.ZIP", usnake(() -> p.getHome().getZip()));
        assertEquals("HOME.CITY", usnake(() -> p.getHome().getCity()));
        assertEquals("HOME.COUNTRY", usnake(() -> p.getHome().getCountry()));
        assertEquals("HOME.STREET", usnake(() -> p.getHome().getStreet()));
        assertEquals("HOME.STREET_NUMBER", usnake(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void lowerCaseSnakePropertyName() {
        Person p = lsnake(Person.class);
        assertEquals("first_name", lsnake(p::getFirstName));
        assertEquals("last_name", lsnake(p::getLastName));
        assertEquals("home", lsnake(p::getHome));
        assertEquals("home.zip", lsnake(() -> p.getHome().getZip()));
        assertEquals("home.city", lsnake(() -> p.getHome().getCity()));
        assertEquals("home.country", lsnake(() -> p.getHome().getCountry()));
        assertEquals("home.street", lsnake(() -> p.getHome().getStreet()));
        assertEquals("home.street_number", lsnake(() -> p.getHome().getStreetNumber()));
    }
}