package rlib.util.dictionary;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import rlib.util.pools.Reusable;

/**
 * The entry of {@link LongDictionary}.
 *
 * @author JavaSaBr
 */
public final class LongEntry<V> implements Reusable {

    /**
     * The next entry.
     */
    private LongEntry<V> next;

    /**
     * The value of this entry.
     */
    private V value;

    /**
     * The key of this entry.
     */
    private long key;
    /**
     * The hash of the key.
     */
    private int hash;

    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != LongEntry.class) return false;

        final LongEntry<?> entry = (LongEntry<?>) object;

        final long firstKey = getKey();
        final long secondKey = entry.getKey();

        if (firstKey != secondKey) return false;

        final Object firstValue = getValue();
        final Object secondValue = entry.getValue();

        return Objects.equals(firstValue, secondValue);
    }

    @Override
    public void free() {
        value = null;
        next = null;
    }

    /**
     * @return the hash of the key.
     */
    public int getHash() {
        return hash;
    }

    /**
     * @return the key of this entry.
     */
    public long getKey() {
        return key;
    }

    /**
     * @return the next entry.
     */
    @Nullable
    public LongEntry<V> getNext() {
        return next;
    }

    /**
     * @param next the next entry.
     */
    public void setNext(@Nullable final LongEntry<V> next) {
        this.next = next;
    }

    /**
     * @return the value of this entry.
     */
    @Nullable
    public V getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return (int) (key ^ (value == null ? 0 : value.hashCode()));
    }

    public void set(final int hash, final long key, final V value, final LongEntry<V> next) {
        this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
    }

    /**
     * @param value the new value of this entry.
     * @return the old value of null.
     */
    @Nullable
    public V setValue(@Nullable final V value) {
        final V old = getValue();
        this.value = value;
        return old;
    }

    @Override
    public String toString() {
        return "LongEntry{" +
                "next=" + next +
                ", value=" + value +
                ", key=" + key +
                ", hash=" + hash +
                '}';
    }
}