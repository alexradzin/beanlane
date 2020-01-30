package org.beanlane;

import org.beanlane.formatter.CaseFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@VisibleForPackage
@BeanNameAnnotation(separator = "/", formatter = @BeanNameFormatter(value = CaseFormatter.class, args = "LOWER"))
class BeanLaneBeanSpecWithAnnotationTest implements BeanLaneBeanSpec {
    @Test
    void shortNotation() {
        Person p = $(Person.class);
        assertEquals("firstname", $(p::getFirstName));
        assertEquals("lastname", $(p::getLastName));
        assertEquals("home", $(p::getHome));
        assertEquals("home/zip", $(() -> p.getHome().getZip()));
        assertEquals("home/city", $(() -> p.getHome().getCity()));
        assertEquals("home/country", $(() -> p.getHome().getCountry()));
        assertEquals("home/street", $(() -> p.getHome().getStreet()));
        assertEquals("home/streetnumber", $(() -> p.getHome().getStreetNumber()));
    }

    @Test
    void longNotation() {
        Person p = wrap(Person.class);
        assertEquals("firstname", name(p::getFirstName));
        assertEquals("lastname", name(p::getLastName));
        assertEquals("home", name(p::getHome));
        assertEquals("home/zip", name(() -> p.getHome().getZip()));
        assertEquals("home/city", name(() -> p.getHome().getCity()));
        assertEquals("home/country", name(() -> p.getHome().getCountry()));
        assertEquals("home/street", name(() -> p.getHome().getStreet()));
        assertEquals("home/streetnumber", name(() -> p.getHome().getStreetNumber()));
    }
}