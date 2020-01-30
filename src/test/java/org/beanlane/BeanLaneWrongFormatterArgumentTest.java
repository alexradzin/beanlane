package org.beanlane;

import org.beanlane.formatter.CaseFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BeanNameAnnotation(formatter = @BeanNameFormatter(value = CaseFormatter.class, args = "WRONG"))
@VisibleForPackage
class BeanLaneWrongFormatterArgumentTest implements BeanLaneBeanSpec {
    @Test
    void test() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> $(Person.class));
        assertTrue(e.getMessage().contains("WRONG"));
    }
}


