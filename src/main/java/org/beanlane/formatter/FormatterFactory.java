package org.beanlane.formatter;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

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
        parsers.put(boolean.class, s -> {
            if ("true".equals(s)) {
                return true;
            } else if ("false".equals(s)) {
                return false;
            } else {
                throw new IllegalArgumentException(s);
            }
        });
        parsers.put(char.class, s -> s.charAt(0));
        parsers.put(String.class, s -> s);
        parsers.put(Class.class, s -> {
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        });
        parsers.put(Pattern.class, Pattern::compile);
    }
    private static final Map<Class, Integer> typeWeights = new HashMap<>();
    static {
        typeWeights.put(byte.class, 1);
        typeWeights.put(short.class,2);
        typeWeights.put(int.class, 4);
        typeWeights.put(long.class, 8);
        typeWeights.put(boolean.class, 0);
        typeWeights.put(Byte.class, 1);
        typeWeights.put(Short.class,2);
        typeWeights.put(Integer.class, 4);
        typeWeights.put(Long.class, 8);
        typeWeights.put(Boolean.class, 0);
        typeWeights.put(char.class, Integer.MAX_VALUE - 1);
        typeWeights.put(Character.class, Integer.MAX_VALUE - 1);
        typeWeights.put(String.class, Integer.MAX_VALUE);
    }

    private static class TypeComparator implements Comparator<Class> {
        @Override
        public int compare(Class c1, Class c2) {
            return weight(c1) - weight(c2);
        }

        private int weight(Class c) {
            return typeWeights.computeIfAbsent(c, Object::hashCode);
        }
    }

    private static class TypesComparator implements Comparator<Class[]> {
        private final TypeComparator tc = new TypeComparator();
        @Override
        public int compare(Class[] c1, Class[] c2) {
            int n = Math.min(c1.length, c2.length);
            for (int i = 0; i < n; i++) {
                int r = tc.compare(c1[i], c2[i]);
                if (r != 0) {
                    return r;
                }
            }
            return 0;
        }
    }

    private static class ConstructorComparator implements Comparator<Constructor> {
        private final TypesComparator tc = new TypesComparator();
        @Override
        public int compare(Constructor c1, Constructor c2) {
            return tc.compare(c1.getParameterTypes(), c2.getParameterTypes());
        }
    }

    public static Function<String, String> create(Class<? extends Function<String, String>> clazz, Class<?>[] paramTypes, String[] strArgs) {
        if (paramTypes == null) {
            Exception ex = null;
            for (Constructor c : stream(clazz.getDeclaredConstructors()).sorted(new ConstructorComparator()).filter(c -> c.getParameterCount() == strArgs.length).collect(toList())) {
                Object[] args = parse(strArgs, c.getParameterTypes());
                if (args != null) {
                    try {
                        c.setAccessible(true);
                        //noinspection unchecked
                        return (Function<String, String>) c.newInstance(args);
                    } catch (ReflectiveOperationException | IllegalArgumentException e) {
                        ex = e;
                    }
                }
            }
            throw new IllegalArgumentException(ex == null ? new NoSuchMethodException(clazz.getName() + "@" + Arrays.toString(strArgs)) : ex);
        }
        try {
            return clazz.getDeclaredConstructor(paramTypes).newInstance(parse(strArgs, paramTypes));
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static Object[] parse(String[] strArgs, Class[] paramTypes) {
        int n = strArgs.length;
        Object[] args = new Object[n];
        for (int i = 0; i < n; i++) {
            Class t = paramTypes[i];
            String s = strArgs[i];

            Function<String, Object> p = parsers.get(paramTypes[i]);
            try {
                if (p != null) {
                    args[i] = p.apply(s);
                } else if (Enum.class.isAssignableFrom(t)) {
                    //noinspection unchecked
                    args[i] = Enum.valueOf(t, s);
                } else {
                    return null; // this argument cannot be parsed to current type
                }
            } catch (RuntimeException e) {
                return null;
            }
        }

        return args;
    }
}
