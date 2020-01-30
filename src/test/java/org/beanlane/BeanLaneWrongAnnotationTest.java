package org.beanlane;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanNameAnnotation(separator = "/", formatter = @BeanNameFormatter(value = BeanLaneWrongAnnotationTest.UnsupportedFormatter.class, args = "1"))
@VisibleForPackage
class BeanLaneWrongAnnotationTest implements BeanLaneBeanSpec {
    @Test
    void test() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> $(Person.class));
        assertEquals(NoSuchMethodException.class, e.getCause().getClass());
    }

    /**
     * Special formatter that has the only constructor that requires parameter of unsupported type.
     */
    public static class UnsupportedFormatter implements Function<String, String> {
        public UnsupportedFormatter(BeanLaneWrongAnnotationTest test) {

        }

        @Override
        public String apply(String s) {
            return null;
        }
    }
}


