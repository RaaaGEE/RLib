package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * Базовая реализация {@link Array}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractArray<E> implements Array<E> {

    private static final long serialVersionUID = 2113052245369887690L;

    /**
     * Размер массива по умолчанию.
     */
    protected static final int DEFAULT_SIZE = 10;

    /**
     * @param type тип элементов в массиве.
     */
    public AbstractArray(final Class<E> type) {
        this(type, DEFAULT_SIZE);
    }

    /**
     * @param type тип элементов в массиве.
     * @param size размер массива.
     */
    public AbstractArray(final Class<E> type, final int size) {
        super();

        if (size < 0) {
            throw new IllegalArgumentException("negative size");
        }

        setArray(unsafeCast(java.lang.reflect.Array.newInstance(type, size)));
    }

    @NotNull
    @Override
    public Array<E> clear() {

        if (size() > 0) {
            ArrayUtils.clear(array());
            setSize(0);
        }

        return this;
    }

    @Override
    public final void free() {
        clear();
    }

    /**
     * @param array массив элементов.
     */
    protected abstract void setArray(E[] array);

    /**
     * @param size размер массива.
     */
    protected abstract void setSize(int size);

    @Override
    public final boolean slowRemove(@NotNull final Object object) {
        return slowRemove(indexOf(object)) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " size = " + size() + " : " + ArrayUtils.toString(this);
    }
}
