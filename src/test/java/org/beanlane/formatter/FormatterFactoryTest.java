package org.beanlane.formatter;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FormatterFactoryTest {
    @Test
    void primitiveArguments() {
        ((FormatWithPrimitiveArguments)FormatterFactory.create(
                FormatWithPrimitiveArguments.class,
                new String[] {"42", "4242", "424242", "42424242", "4", "true"}))
                .assertValues((byte)42, (short)4242, 424242, 42424242L, '4', true);
    }


    @Test
    void classArgument() {
        assertEquals(getClass(), ((FormatWithClassArgument)FormatterFactory.create(
                FormatWithClassArgument.class,
                new String[] {getClass().getName()})).getClazz()
        );
    }

    @Test
    void wrongClassArgument() {
        assertEquals(ClassNotFoundException.class, assertThrows(IllegalArgumentException.class, () -> FormatterFactory.create(FormatWithClassArgument.class, new String[] {"com.nothing.NotExisting"})).getCause().getClass());
    }




    private static class FormatWithPrimitiveArguments implements Function<String, String> {
        private final byte b;
        private final short s;
        private final int i;
        private final long l;
        private final char c;
        private final boolean flag;

        private FormatWithPrimitiveArguments(byte b, short s, int i, long l, char c, boolean flag) {
            this.b = b;
            this.s = s;
            this.i = i;
            this.l = l;
            this.c = c;
            this.flag = flag;
        }

        void assertValues(byte b, short s, int i, long l, char c, boolean flag) {
            assertEquals(this.b, b);
            assertEquals(this.s, s);
            assertEquals(this.i, i);
            assertEquals(this.l, l);
            assertEquals(this.c, c);
            assertEquals(this.flag, flag);

        }

        @Override
        public String apply(String s) {
            return null;
        }
    }

    private static class FormatWithClassArgument implements Function<String, String> {
        private final Class<?> clazz;

        private FormatWithClassArgument(Class<?> clazz) {
            this.clazz = clazz;
        }

        private Class<?> getClazz() {
            return clazz;
        }

        @Override
        public String apply(String s) {
            return null;
        }
    }


}