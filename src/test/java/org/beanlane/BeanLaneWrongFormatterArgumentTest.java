package org.beanlane;

import org.beanlane.formatter.CaseFormatter;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanName(formatter = @BeanNameFormatter(value = CaseFormatter.class, args = "WRONG"))
@VisibleForPackage
class BeanLaneWrongFormatterArgumentTest implements BeanLaneBeanSpec {
    @Test
    void test() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> $(Person.class));
        assertEquals("No enum constant org.beanlane.formatter.CaseFormatter.Case.WRONG", e.getMessage());
    }
}


