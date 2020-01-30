package org.beanlane;

import org.beanlane.extractor.BeanNameAnnotationExtractor;
import org.beanlane.extractor.BeanNameExtractor;
import org.beanlane.formatter.FormatterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
        BeanNameAnnotation[] annotations = specClass.getAnnotationsByType(BeanNameAnnotation.class);

        if (annotations == null || annotations.length == 0) {
            throw new IllegalArgumentException(format("Class %s is not marked with annotation %s", getClass().getName(), BeanNameAnnotation.class.getName()));
        }
        Collection<String> separators = Arrays.stream(annotations).map(BeanNameAnnotation::separator).distinct().collect(Collectors.toList());
        if (separators.size() > 1) {
            throw new IllegalArgumentException("All annotations must use the same separator but were different: " + separators);
        }

        final String separator;
        Iterator<String> it = separators.iterator();
        if (it.hasNext()) {
            String confSeparator = it.next();
            if (BeanNameAnnotation.DEFAULT_SEPARATOR.equals(confSeparator)) {
                confSeparator = BeanLane.DEFAULT_SEPARATOR;
            }
            separator = confSeparator;
        } else {
            separator = BeanLane.DEFAULT_SEPARATOR;
        }


        Map<Function<String, String>, Function<Method, String>> formatterToExtractor = new LinkedHashMap<>();
        for (BeanNameAnnotation a : annotations) {
            Function<String, String> formatter = new ChainedFunction<>(
                    Arrays.stream(a.formatter())
                            .map(conf -> FormatterFactory.create(conf.value(), paramTypes(conf), conf.args()))
                            .collect(Collectors.toList()));

            Class<? extends Annotation> aClass = a.value();
            Function<Method, String> nameExtractor = BeanNameAnnotation.class.equals(aClass) ?
                    new BeanNameExtractor(formatter) :
                    new BeanNameAnnotationExtractor(aClass, a.field(), formatter);
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

//    static String getSeparator(Supplier<String> separatorSupplier) {
//        String separator = separatorSupplier.get();
//        return BeanNameAnnotation.DEFAULT_SEPARATOR.equals(separator) ? BeanLane.DEFAULT_SEPARATOR : separator;
//    }

    static Class[] paramTypes(BeanNameFormatter conf) {
        return conf.paramTypes().length == 1 && BeanNameFormatter.class.equals(conf.paramTypes()[0]) ? null : conf.paramTypes();
    }

}
