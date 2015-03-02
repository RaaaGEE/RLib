package rlib.util.ref;

import static rlib.util.ref.ReferenceType.SHORT;

/**
 * Ссылка на тип данных short.
 * 
 * @author Ronn
 */
final class ShortReference extends AbstractReference {

	/** значение по ссылке */
	private short value;

	@Override
	public ReferenceType getReferenceType() {
		return SHORT;
	}

	@Override
	public short getShort() {
		return value;
	}

	@Override
	public void setShort(final short value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [value=" + value + "]";
	}
}
