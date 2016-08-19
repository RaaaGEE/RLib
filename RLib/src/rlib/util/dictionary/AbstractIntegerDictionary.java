package rlib.util.dictionary;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import rlib.function.IntBiObjectConsumer;
import rlib.function.IntObjectConsumer;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * Базовая реализация словаря с примитивным ключем int.
 *
 * @author JavaSaBr
 */
public abstract class AbstractIntegerDictionary<V> extends AbstractDictionary<IntKey, V> implements UnsafeIntegerDictionary<V> {

    /**
     * Пул ячеяк.
     */
    private final ReusablePool<IntegerEntry<V>> entryPool;

    /**
     * Фактор загруженности.
     */
    private final float loadFactor;

    /**
     * Таблица элементов.
     */
    private IntegerEntry<V>[] content;

    /**
     * Следующий размер для метода изминения размера (capacity * load factor).
     */
    private int threshold;

    protected AbstractIntegerDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.threshold = (int) (initCapacity * loadFactor);
        this.content = new IntegerEntry[Dictionary.DEFAULT_INITIAL_CAPACITY];
        this.entryPool = PoolFactory.newReusablePool(IntegerEntry.class);
    }

    /**
     * Добавляет новую ячейку в таблицу.
     *
     * @param hash  хэш значение.
     * @param key   значение ключа.
     * @param value значение по ключу.
     * @param index индекс ячейки.
     */
    protected final void addEntry(final int hash, final int key, final V value, final int index) {

        final ReusablePool<IntegerEntry<V>> entryPool = getEntryPool();

        final IntegerEntry<V>[] table = content();
        final IntegerEntry<V> entry = table[index];

        final IntegerEntry<V> newEntry = entryPool.take(IntegerEntry::new);
        newEntry.set(hash, key, value, entry);

        table[index] = newEntry;

        if (incrementSizeAndGet() >= threshold) {
            resize(2 * table.length);
        }
    }

    @Override
    public void apply(final Function<? super V, V> function) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        final ReusablePool<IntegerEntry<V>> entryPool = getEntryPool();
        final IntegerEntry<V>[] content = content();

        IntegerEntry<V> next;

        for (IntegerEntry<V> entry : content) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.put(entry);
                entry = next;
            }
        }

        ArrayUtils.clear(content);
    }

    @Override
    public final boolean containsKey(final int key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(final V value) {

        if (value == null) {
            throw new NullPointerException("value is null.");
        }

        for (final IntegerEntry<V> element : content()) {
            for (IntegerEntry<V> entry = element; entry != null; entry = entry.getNext()) {
                if (value.equals(entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return массив ячеяк.
     */
    @Override
    public final IntegerEntry<V>[] content() {
        return content;
    }

    @Override
    public void forEach(final Consumer<? super V> consumer) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <T> void forEach(final T argument, final IntBiObjectConsumer<V, T> consumer) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void forEach(final IntObjectConsumer<V> consumer) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V get(final int key) {
        final IntegerEntry<V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V get(final int key, final Supplier<V> factory) {

        IntegerEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
        }

        entry = getEntry(key);

        return entry == null ? null : entry.getValue();
    }

    @Override
    public V get(final int key, final IntFunction<V> factory) {

        IntegerEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
        }

        entry = getEntry(key);

        return entry == null ? null : entry.getValue();
    }

    @Override
    public <T> V get(final int key, final T argument, final Function<T, V> factory) {

        IntegerEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
        }

        entry = getEntry(key);

        return entry == null ? null : entry.getValue();
    }

    /**
     * Получение ячейки по ключу.
     *
     * @param key ключ ячейки.
     * @return ячейка.
     */
    private IntegerEntry<V> getEntry(final int key) {

        final int hash = hash(key);

        final IntegerEntry<V>[] table = content();

        for (IntegerEntry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry;
            }
        }

        return null;
    }

    /**
     * @return пул ячеяк.
     */
    private ReusablePool<IntegerEntry<V>> getEntryPool() {
        return entryPool;
    }

    @Override
    public DictionaryType getType() {
        return DictionaryType.INTEGER;
    }

    @Override
    public final Iterator<V> iterator() {
        return new IntegerDictionaryIterator<>(this);
    }

    @Override
    public IntegerArray keyIntegerArray(final IntegerArray container) {

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void moveTo(final Dictionary<? super IntKey, ? super V> dictionary) {

        if (isEmpty() || dictionary.getType() != getType()) {
            return;
        }

        final IntegerDictionary<V> integerDictionary = (IntegerDictionary<V>) dictionary;

        super.moveTo(dictionary);

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                integerDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(final int key, final V value) {

        final int hash = hash(key);

        final IntegerEntry<V>[] content = content();

        final int i = indexFor(hash, content.length);

        for (IntegerEntry<V> entry = content[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);

        return null;
    }

    @Override
    public final V remove(final int key) {

        final IntegerEntry<V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<IntegerEntry<V>> entryPool = getEntryPool();
        entryPool.put(old);

        return value;
    }

    /**
     * Удаление значения из ячейки по указанному ключу.
     *
     * @param key ключ ячейки.
     * @return удаленная ячейка.
     */
    @Override
    public final IntegerEntry<V> removeEntryForKey(final int key) {

        final int hash = hash(key);

        final IntegerEntry<V>[] content = content();

        final int i = indexFor(hash, content.length);

        IntegerEntry<V> prev = content[i];
        IntegerEntry<V> entry = prev;

        while (entry != null) {

            final IntegerEntry<V> next = entry.getNext();

            if (entry.getHash() == hash && key == entry.getKey()) {
                decrementSizeAndGet();

                if (prev == entry) {
                    content[i] = next;
                } else {
                    prev.setNext(next);
                }

                return entry;
            }

            prev = entry;
            entry = next;
        }

        return null;
    }

    /**
     * Перестройка таблицы под новый размер.
     *
     * @param newLength новый размер.
     */
    private void resize(final int newLength) {

        final IntegerEntry<V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        final IntegerEntry<V>[] newContent = new IntegerEntry[newLength];
        transfer(newContent);

        this.content = newContent;
        this.threshold = (int) (newLength * loadFactor);
    }

    @Override
    public final String toString() {

        final int size = size();

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" : ");

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]");
                builder.append("\n");
                entry = entry.getNext();
            }
        }

        if (size > 0) {
            builder.replace(builder.length() - 1, builder.length(), ".");
        }

        return builder.toString();
    }

    /**
     * Перенос всех записей из старой таблице в новую.
     *
     * @param newTable новая таблица.
     */
    private void transfer(final IntegerEntry<V>[] newTable) {

        final int newCapacity = newTable.length;

        for (IntegerEntry<V> entry : content()) {
            if (entry != null) {
                do {

                    final IntegerEntry<V> next = entry.getNext();

                    final int i = indexFor(entry.getHash(), newCapacity);

                    entry.setNext(newTable[i]);
                    newTable[i] = entry;
                    entry = next;

                } while (entry != null);
            }
        }
    }

    @Override
    public Array<V> values(final Array<V> container) {
        container.prepareForSize(container.size() + size());

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                container.unsafeAdd(entry.getValue());
                entry = entry.getNext();
            }
        }

        return container;
    }
}
