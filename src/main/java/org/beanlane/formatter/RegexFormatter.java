package org.beanlane.formatter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexFormatter implements Function<String, String> {
    private final Pattern pattern;
    private final String replacement;
    private final BiFunction<Matcher, String, String> action;
    private static final Map<Character, Integer> options = new HashMap<>();
    static {
        options.put('i', Pattern.CASE_INSENSITIVE);
        options.put('m', Pattern.MULTILINE);
        options.put('o', Pattern.DOTALL);
    }
    private static final BiFunction<Matcher, String, String> replaceFirst = Matcher::replaceFirst;
    private static final BiFunction<Matcher, String, String> replaceAll = Matcher::replaceAll;


    public RegexFormatter(String regex, String options, String replacement) {
        this(regex, options(options), replacement, options.contains("g") ? replaceAll : replaceFirst);
    }

    public RegexFormatter(String regex, int options, String replacement, BiFunction<Matcher, String, String> action) {
        this(Pattern.compile(regex, options), replacement, action);
    }

    public RegexFormatter(Pattern pattern, String replacement, BiFunction<Matcher, String, String> action) {
        this.pattern = pattern;
        this.replacement = replacement;
        this.action = action;
    }

    private static int options(String s) {
        int result = 0;
        for (char c : s.toCharArray()) {
            Integer i = options.get(c);
            if (i != null) {
                result |= i;
            }
        }
        return result;
    }

    @Override
    public String apply(String input) {
        return action.apply(pattern.matcher(input), replacement);
    }
}
