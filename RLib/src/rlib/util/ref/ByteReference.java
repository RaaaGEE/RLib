package rlib.util.ref;

import static rlib.util.ref.ReferenceType.BYTE;

/**
 * Ссылка на тип данных byte.
 *
 * @author JavaSaBr
 */
final class ByteReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private byte value;

    @Override
    public byte getByte() {
        return value;
    }

    @Override
    public void setByte(final byte value) {
        this.value = value;
    }

    @Override
    public ReferenceType getReferenceType() {
        return BYTE;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
