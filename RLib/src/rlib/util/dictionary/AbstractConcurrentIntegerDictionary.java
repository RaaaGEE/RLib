package rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentIntegerDictionary}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements ConcurrentIntegerDictionary<V> {

    /**
     * Тhe array of entries.
     */
    private volatile IntegerEntry<V>[] content;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private volatile int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private volatile int size;

    protected AbstractConcurrentIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentIntegerDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentIntegerDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    protected AbstractConcurrentIntegerDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public void setContent(@NotNull final IntegerEntry<V>[] content) {
        this.content = content;
    }

    @NotNull
    @Override
    public IntegerEntry<V>[] content() {
        return content;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    protected void setSize(final int size) {
        this.size = size;
    }

    @Override
    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected int decrementSizeAndGet() {
        size -= 1;
        return size;
    }

    @Override
    protected int incrementSizeAndGet() {
        size += 1;
        return size;
    }

    @Override
    public final int size() {
        return size;
    }
}
