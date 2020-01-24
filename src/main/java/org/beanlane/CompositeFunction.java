package org.beanlane;

import java.util.Collection;
import java.util.function.Function;

import static java.lang.String.format;

public class CompositeFunction<T> implements Function<T, T> {
    private final Collection<Function<T, T>> functions;

    public CompositeFunction(Collection<Function<T, T>> functions) {
        this.functions = functions;
    }

    @Override
    public T apply(T t) {
        T result = t;
        for (Function<T, T> f : functions) {
            result = f.apply(result);
        }
        return result;
    }

    @Override
    public String toString() {
        return format("%s:%s", getClass(), functions);
    }
}
