package org.beanlane;


import org.beanlane.formatter.ToSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanPropertyExtractor(value = XmlElement.class, field = "name", formatter = {@BeanPropertyFormatter(value = ToSnakeCaseFormatter.class, args="-")})
@VisibleForPackage
class BeanLaneAnnotationWithDashSnakeFormatterTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyShortName() {
        Person p = $(Person.class);
        assertEquals("first-name", $(p::getFirstName));
        assertEquals("last-name", $(p::getLastName));
        assertEquals("home-address", $(p::getHome));
        assertEquals("home-address.zip", $(() -> p.getHome().getZip()));
        assertEquals("home-address.city", $(() -> p.getHome().getCity()));
        assertEquals("home-address.country", $(() -> p.getHome().getCountry()));
        assertEquals("home-address.street", $(() -> p.getHome().getStreet()));
        assertEquals("home-address.street-number", $(() -> p.getHome().getStreetNumber()));
        assertEquals("letters", $(p::getCorrespondence));
        assertEquals("letters.zip", $(() -> p.getCorrespondence().getZip()));
        assertEquals("spouse.first-name", $(() -> p.getSpouse().getFirstName()));
    }


    @Test
    void beanPropertyName() {
        Person p = wrap(Person.class);
        assertEquals("first-name", name(p::getFirstName));
        assertEquals("last-name", name(p::getLastName));
        assertEquals("home-address", name(p::getHome));
        assertEquals("home-address.zip", name(() -> p.getHome().getZip()));
        assertEquals("home-address.city", name(() -> p.getHome().getCity()));
        assertEquals("home-address.country", name(() -> p.getHome().getCountry()));
        assertEquals("home-address.street", name(() -> p.getHome().getStreet()));
        assertEquals("home-address.street-number", name(() -> p.getHome().getStreetNumber()));
        assertEquals("letters", name(p::getCorrespondence));
        assertEquals("letters.zip", name(() -> p.getCorrespondence().getZip()));
        assertEquals("spouse.first-name", name(() -> p.getSpouse().getFirstName()));
    }

    @Test
    void notField() {
        Address a = $(Address.class);
        assertThrows(UnsupportedOperationException.class, a::getZipCode);
    }
}
