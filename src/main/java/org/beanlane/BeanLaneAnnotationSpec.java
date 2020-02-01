package org.beanlane;

import org.beanlane.extractor.PropertyNameAnnotationExtractor;
import org.beanlane.extractor.PropertyNameExtractor;
import org.beanlane.formatter.FormatterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;

public interface BeanLaneAnnotationSpec {
    Map<Class, BeanLane> br = new ConcurrentHashMap<>(1);

    default <T> T $(Class<T> clazz) {
        return wrap(clazz);
    }

    default <T> T wrap(Class<T> clazz) {
        Class<?> specClass = getClass();

        List<BeanPropertyExtractor> extractors = new ArrayList<>();
        for (Annotation a : specClass.getAnnotations()) {
            if (a instanceof BeanPropertyExtractor) {
                extractors.add((BeanPropertyExtractor)a);
            } else if (a instanceof BeanPropertyExtractors) {
                extractors.addAll(Arrays.asList(((BeanPropertyExtractors)a).value()));
            } else {
                extractors.addAll(Arrays.asList(a.annotationType().getAnnotationsByType(BeanPropertyExtractor.class)));
            }
        }


        if (extractors.isEmpty()) {
            throw new IllegalArgumentException(format("Class %s is not marked with annotation %s", getClass().getName(), BeanPropertyExtractor.class.getName()));
        }
        Collection<String> separators = extractors.stream().map(BeanPropertyExtractor::separator).distinct().collect(Collectors.toList());
        if (separators.size() > 1) {
            throw new IllegalArgumentException("All annotations must use the same separator but were different: " + separators);
        }

        final String separator;
        Iterator<String> it = separators.iterator();
        if (it.hasNext()) {
            String confSeparator = it.next();
            if (BeanPropertyExtractor.DEFAULT_SEPARATOR.equals(confSeparator)) {
                confSeparator = BeanLane.DEFAULT_SEPARATOR;
            }
            separator = confSeparator;
        } else {
            separator = BeanLane.DEFAULT_SEPARATOR;
        }


        Map<Function<String, String>, Function<Method, String>> formatterToExtractor = new LinkedHashMap<>();
        for (BeanPropertyExtractor e : extractors) {
            Function<String, String> formatter = new ChainedFunction<>(
                    Arrays.stream(e.formatter())
                            .map(conf -> FormatterFactory.create(conf.value(), paramTypes(conf), conf.args()))
                            .collect(Collectors.toList()));

            Class<? extends Annotation> aClass = e.value();
            Function<Method, String> nameExtractor = BeanPropertyExtractor.class.equals(aClass) ?
                    new PropertyNameExtractor(formatter) :
                    new PropertyNameAnnotationExtractor(aClass, e.field(), formatter);
            formatterToExtractor.put(formatter, nameExtractor);
        }


        return br.computeIfAbsent(specClass, s -> new BeanLane(separator, new FirstTakeFunction<>(formatterToExtractor.values()))).of(clazz);
    }


    default <T> String name(Supplier<T> f)  {
        return $(f);
    }

    default <T> String $(Supplier<T> f)  {
        return br.get(getClass()).name(f);
    }

    static Class[] paramTypes(BeanPropertyFormatter conf) {
        return conf.paramTypes().length == 1 && BeanPropertyFormatter.class.equals(conf.paramTypes()[0]) ? null : conf.paramTypes();
    }

}
