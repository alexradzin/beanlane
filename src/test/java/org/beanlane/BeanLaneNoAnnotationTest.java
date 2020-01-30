package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@BeanPropertyExtractor - this annotation is mandatory for classes that implement BeanLaneAnnotationSpec
@VisibleForPackage
class BeanLaneNoAnnotationTest implements BeanLaneAnnotationSpec {
    @Test
    void beanPropertyWrongName() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> $(Person.class));
        assertEquals(String.format("Class %s is not marked with annotation %s", getClass().getName(), BeanPropertyExtractor.class.getName()), e.getMessage());
    }
}
