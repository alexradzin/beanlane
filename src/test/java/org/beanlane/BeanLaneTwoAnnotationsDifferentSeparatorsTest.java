package org.beanlane;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanPropertyExtractor(separator = "/", value = JsonProperty.class)
@BeanPropertyExtractor(separator = "#", value = XmlElement.class, field = "name")
@VisibleForPackage
class BeanLaneTwoAnnotationsDifferentSeparatorsTest implements BeanLaneAnnotationSpec {
    @Test
    void differentSeparators() {
        assertEquals(
                "All annotations must use the same separator but were different: [/, #]",
                assertThrows(IllegalArgumentException.class, () -> $(Address.class)).getMessage());
    }
}