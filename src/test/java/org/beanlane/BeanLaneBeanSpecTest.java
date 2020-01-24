package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@VisibleForPackage
class BeanLaneBeanSpecTest implements BeanLaneBeanSpec {
    @Test
    void shortNotation() {
        Person p = $(Person.class);
        assertEquals("firstName", $(p::getFirstName));
        assertEquals("lastName", $(p::getLastName));
        assertEquals("home", $(p::getHome));
        assertEquals("home.zip", $(() -> p.getHome().getZip()));
        assertEquals("home.city", $(() -> p.getHome().getCity()));
        assertEquals("home.country", $(() -> p.getHome().getCountry()));
        assertEquals("home.street", $(() -> p.getHome().getStreet()));
        assertEquals("home.streetNumber", $(() -> p.getHome().getStreetNumber()));
        assertEquals("home.capital", $(() -> p.getHome().isCapital()));
        assertEquals("correspondence", $(p::getCorrespondence));
        assertEquals("correspondence.zip", $(() -> p.getCorrespondence().getZip()));
        assertEquals("spouse.firstName", $(() -> p.getSpouse().getFirstName()));
    }

    @Test
    void longNotation() {
        Person p = wrap(Person.class);
        assertEquals("firstName", name(p::getFirstName));
        assertEquals("lastName", name(p::getLastName));
        assertEquals("home", name(p::getHome));
        assertEquals("home.zip", name(() -> p.getHome().getZip()));
        assertEquals("home.city", name(() -> p.getHome().getCity()));
        assertEquals("home.country", name(() -> p.getHome().getCountry()));
        assertEquals("home.street", name(() -> p.getHome().getStreet()));
        assertEquals("home.streetNumber", name(() -> p.getHome().getStreetNumber()));
        assertEquals("correspondence", name(p::getCorrespondence));
        assertEquals("correspondence.zip", name(() -> p.getCorrespondence().getZip()));
        assertEquals("spouse.firstName", name(() -> p.getSpouse().getFirstName()));
    }
}