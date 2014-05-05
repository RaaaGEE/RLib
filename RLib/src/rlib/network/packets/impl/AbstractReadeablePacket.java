package rlib.network.packets.impl;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.packets.ReadeablePacket;
import rlib.util.Util;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;

/**
 * Базовая модель сетевого читаемого пакета.
 * 
 * @author Ronn
 */
public abstract class AbstractReadeablePacket<C> extends AbstractPacket<C> implements ReadeablePacket<C>, Foldable {

	protected static final Logger LOGGER = LoggerManager.getLogger(ReadeablePacket.class);

	@Override
	public final int getAvaliableBytes() {
		return buffer.remaining();
	}

	/**
	 * @return пул этого класса пакетов.
	 */
	@SuppressWarnings("rawtypes")
	protected abstract FoldablePool getPool();

	@Override
	public boolean isSynchronized() {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadeablePacket<C> newInstance() {
		return (ReadeablePacket<C>) getPool().take();
	}

	@Override
	public final boolean read() {

		try {
			readImpl();
			return true;
		} catch(final Exception e) {
			LOGGER.warning(this, e);
			LOGGER.warning(this, "buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.limit()));
		}

		return false;
	}

	/**
	 * Чтение одного байта из буфера.
	 */
	public final int readByte() {
		return buffer.get() & 0xFF;
	}

	/**
	 * Наполнение указанного массива байтов, байтами из буфера.
	 * 
	 * @param array наполняемый массив байтов.
	 */
	public final void readBytes(final byte[] array) {
		buffer.get(array);
	}

	/**
	 * Наполнение указанного массива байтов, байтами из буфера.
	 * 
	 * @param array наполняемый массив байтов.
	 * @param offset отступ в массиве байтов.
	 * @param length кол-во записываемых байтов в массив.
	 */
	public final void readBytes(final byte[] array, final int offset, final int length) {
		buffer.get(array, offset, length);
	}

	/**
	 * Чтение 4х байтов в виде float из буфера.
	 */
	public final float readFloat() {
		return buffer.getFloat();
	}

	/**
	 * Процесс чтения пакета.
	 */
	protected abstract void readImpl();

	/**
	 * Чтение 4х байтов в виде int из буфера.
	 */
	public final int readInt() {
		return buffer.getInt();
	}

	/**
	 * Чтение 8ми байтов в виде long из буфера.
	 */
	public final long readLong() {
		return buffer.getLong();
	}

	/**
	 * Чтение 2х байтов в виде short из буфера.
	 */
	public final int readShort() {
		return buffer.getShort() & 0xFFFF;
	}

	/**
	 * Чтение строки из буфера.
	 */
	public final String readString() {

		final StringBuilder builder = new StringBuilder();

		char cha;

		while(buffer.remaining() > 1) {

			cha = buffer.getChar();

			if(cha == 0) {
				break;
			}

			builder.append(cha);
		}

		return builder.toString();
	}

	/**
	 * Чтение строки из буфера указанной длинны.
	 */
	public final String readString(final int length) {

		final char[] array = new char[length];

		for(int i = 0; i < length && buffer.remaining() > 1; i++) {
			array[i] = buffer.getChar();
		}

		return new String(array);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			runImpl();
		} catch(final Exception e) {
			LOGGER.warning(this, e);
		} finally {
			getPool().put(this);
		}
	}

	/**
	 * Процесс выполнение пакета.
	 */
	protected abstract void runImpl();
}