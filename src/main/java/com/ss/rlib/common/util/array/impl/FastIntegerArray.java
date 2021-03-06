package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.IntegerArray;

/**
 * Реализация не потокобезопасного динамического массива примитивов int.
 *
 * @author JavaSaBr
 */
public class FastIntegerArray implements IntegerArray {

    /**
     * Массив элементов.
     */
    protected int[] array;

    /**
     * Кол-во элементов в колекции.
     */
    protected int size;

    /**
     * Instantiates a new Fast integer array.
     */
    public FastIntegerArray() {
        this(10);
    }

    /**
     * Instantiates a new Fast integer array.
     *
     * @param size the size
     */
    public FastIntegerArray(final int size) {
        this.array = new int[size];
        this.size = 0;
    }

    @Override
    public FastIntegerArray add(final int element) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, Math.max(array.length >> 1, 1));
        }

        array[size++] = element;
        return this;
    }

    @Override
    public final FastIntegerArray addAll(final int[] elements) {

        if (elements == null || elements.length < 1) {
            return this;
        }

        final int current = array.length;
        final int diff = size() + elements.length - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (final int value : elements) {
            add(value);
        }

        return this;
    }

    @Override
    public final FastIntegerArray addAll(final IntegerArray elements) {

        if (elements == null || elements.isEmpty()) {
            return this;
        }

        final int current = array.length;
        final int diff = size() + elements.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        final int[] array = elements.array();

        for (int i = 0, length = elements.size(); i < length; i++) {
            add(array[i]);
        }

        return this;
    }

    @Override
    public final int[] array() {
        return array;
    }

    @Override
    public final FastIntegerArray clear() {
        size = 0;
        return this;
    }

    @Override
    public final boolean fastRemoveByIndex(final int index) {

        if (index < 0 || size < 1 || index >= size) {
            return false;
        }

        final int[] array = array();

        size -= 1;

        array[index] = array[size];
        array[size] = 0;

        return true;
    }

    @Override
    public final int first() {
        return size < 1 ? -1 : array[0];
    }

    @Override
    public final int get(final int index) {
        return array[index];
    }

    @Override
    public final ArrayIterator<Integer> iterator() {
        return new FastIterator();
    }

    @Override
    public final int last() {
        return size < 1 ? -1 : array[size - 1];
    }

    @Override
    public final int poll() {
        final int val = first();
        return slowRemoveByIndex(0) ? val : -1;
    }

    @Override
    public final int pop() {
        final int last = last();
        return fastRemoveByIndex(size - 1) ? last : -1;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean slowRemoveByIndex(final int index) {
        if (index < 0 || size < 1) return false;

        final int[] array = array();

        final int numMoved = size - index - 1;

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        array[--size] = 0;
        return true;
    }

    @Override
    public final FastIntegerArray sort() {
        ArrayUtils.sort(array, 0, size);
        return this;
    }

    @Override
    public final FastIntegerArray trimToSize() {

        int[] array = array();

        if (size == array.length) {
            return this;
        }

        this.array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    private final class FastIterator implements ArrayIterator<Integer> {

        /**
         * текущая позиция в массиве
         */
        private int ordinal;

        /**
         * Instantiates a new Fast iterator.
         */
        public FastIterator() {
            ordinal = 0;
        }

        @Override
        public void fastRemove() {
            FastIntegerArray.this.fastRemove(--ordinal);
        }

        @Override
        public boolean hasNext() {
            return ordinal < size;
        }

        @Override
        public int index() {
            return ordinal - 1;
        }

        @Override
        public Integer next() {
            return array[ordinal++];
        }

        @Override
        public void remove() {
            FastIntegerArray.this.fastRemove(--ordinal);
        }
    }
}