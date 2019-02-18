package org.beanlane;

import java.util.function.Supplier;

public interface BeanLaneLongSpec extends BeanLaneShortSpec {
    default <T> T bean(Class<T> clazz) {
        return $(clazz);
    }

    default <T> String bean(Supplier<T> f)  {
        return $(f);
    }

    default <T> T lsnake(Class<T> clazz) {
        return __(clazz);
    }

    default <T> String lsnake(Supplier<T> f)  {
        return __(f);
    }

    default <T> T usnake(Class<T> clazz) {
        return ___(clazz);
    }

    default <T> String usnake(Supplier<T> f)  {
        return ___(f);
    }
}
