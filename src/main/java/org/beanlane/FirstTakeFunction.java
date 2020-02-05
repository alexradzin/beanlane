package org.beanlane;

import java.util.Collection;
import java.util.function.Function;

public class FirstTakeFunction<T, R> implements Function<T, R> {
    private final Collection<Function<T, R>> functions;

    public FirstTakeFunction(Collection<Function<T, R>> functions) {
        this.functions = functions;
    }

    @Override
    public R apply(T t) {
        UnsupportedOperationException ex = null;
        for (Function<T, R> f : functions) {
            try {
                return f.apply(t);
            } catch (UnsupportedOperationException e) {
                ex = e;
            }
        }
        if (ex != null) {
            throw ex;
        }
        throw new UnsupportedOperationException("None of given functions supports required operation");
    }
}
