package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BeanPropertyExtractor(formatter = {})
@VisibleForPackage
class BeanLaneNoFormattersInExtractorTest implements BeanLaneAnnotationSpec {
    /**
     * Annotation is defined without formatters, so names extracted from getters are used as-is
     */
    @Test
    void beanPropertyWrongName() {
        Person p = $(Person.class);
        assertEquals("getFirstName", $(p::getFirstName));
        assertEquals("getLastName", $(p::getLastName));
        assertEquals("getHome", $(p::getHome));
        assertEquals("getHome.getZip", $(() -> p.getHome().getZip()));
        assertEquals("getHome.getCity", $(() -> p.getHome().getCity()));
        assertEquals("getHome.getCountry", $(() -> p.getHome().getCountry()));
        assertEquals("getHome.getStreet", $(() -> p.getHome().getStreet()));
        assertEquals("getHome.getStreetNumber", $(() -> p.getHome().getStreetNumber()));

    }
}
