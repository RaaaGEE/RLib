package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectLongPredicate<T> {

    /**
     * Test boolean.
     *
     * @param first  the first
     * @param second the second
     * @return the boolean
     */
    boolean test(@Nullable T first, long second);
}
