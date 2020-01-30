package org.beanlane;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = BeanLaneFormatterThrowingExceptionTest.ThrowingFormatter.class, args = "something")})
@VisibleForPackage
class BeanLaneFormatterThrowingExceptionTest implements BeanLaneBeanSpec {
    @Test
    void test() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> $(Person.class));
        assertEquals(InvocationTargetException.class, e.getCause().getClass());
        assertEquals(IllegalStateException.class, e.getCause().getCause().getClass());
        assertEquals("Just wrong something", e.getCause().getCause().getMessage());
    }

    /**
     * Special formatter that has the only constructor that requires parameter of unsupported type.
     */
    public static class ThrowingFormatter implements Function<String, String> {
        public ThrowingFormatter(String arg) {
            throw new IllegalStateException("Just wrong " + arg);
        }

        @Override
        public String apply(String s) {
            return null;
        }
    }

}
