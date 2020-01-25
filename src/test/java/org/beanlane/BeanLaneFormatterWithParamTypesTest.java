package org.beanlane;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BeanName(formatter = @BeanNameFormatter(value = BeanLaneFormatterWithParamTypesTest.TestFormatter.class, paramTypes = String.class, args = "123"))
@VisibleForPackage
class BeanLaneFormatterWithParamTypesTest implements BeanLaneBeanSpec {
    @Test
    void test() {
        $(Person.class);
        assertEquals("string", TestFormatter.constructor);
    }


    public static class TestFormatter implements Function<String, String> {
        private static String constructor;
        public TestFormatter(int i) {
            constructor = "int";
        }

        public TestFormatter(String s) {
            constructor = "string";
        }

        @Override
        public String apply(String s) {
            return null;
        }
    }
}


