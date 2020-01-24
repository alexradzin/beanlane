package org.beanlane.formatter;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class FormatterFactory {
    private static final Map<Class, Function<String, Object>> parsers = new HashMap<>();
    static {
        parsers.put(Byte.class, Byte::parseByte);
        parsers.put(Short.class, Short::parseShort);
        parsers.put(Integer.class, Integer::parseInt);
        parsers.put(Long.class, Long::parseLong);
        parsers.put(Boolean.class, Boolean::parseBoolean);
        parsers.put(Character.class, s -> s.charAt(0));
        parsers.put(byte.class, Byte::parseByte);
        parsers.put(short.class, Short::parseShort);
        parsers.put(int.class, Integer::parseInt);
        parsers.put(long.class, Long::parseLong);
        parsers.put(boolean.class, Boolean::parseBoolean);
        parsers.put(char.class, s -> s.charAt(0));
        parsers.put(String.class, s -> s);
        parsers.put(Class.class, s -> {
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    public static Function<String, String> create(Class<? extends Function<String, String>> clazz, String[] strArgs) {
        Exception ex = null;
        for (Constructor c : stream(clazz.getDeclaredConstructors()).filter(c -> c.getParameterCount() == strArgs.length).collect(toList())) {
            Object[] args = parse(strArgs, c.getParameterTypes());
            if (args != null) {
                try {
                    c.setAccessible(true);
                    //noinspection unchecked
                    return (Function<String, String>)c.newInstance(args);
                } catch (ReflectiveOperationException | IllegalArgumentException e) {
                    ex = e;
                }
            }
        }
        throw new IllegalArgumentException(ex == null ? new NoSuchMethodException(clazz.getName() + "@" + Arrays.toString(strArgs)) : ex);
    }

    private static Object[] parse(String[] strArgs, Class[] paramTypes) {
        int n = strArgs.length;
        Object[] args = new Object[n];
        for (int i = 0; i < n; i++) {
            Class t = paramTypes[i];
            String s = strArgs[i];

            Function<String, Object> p = parsers.get(paramTypes[i]);
            if (p != null) {
                args[i] = p.apply(s);
            } else if (Enum.class.isAssignableFrom(t)) {
                //noinspection unchecked
                args[i] = Enum.valueOf(t, s);
            } else {
                return null; // this argment cannot be parsed to current type
            }
        }

        return args;
    }
}
