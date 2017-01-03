package rlib.idfactory;

/**
 * Интерфейс для реализации генератора ид.
 *
 * @author JavaSaBr
 */
public interface IdGenerator {

    /**
     * @return новый свободный ид.
     */
    public default int getNextId() {
        return 0;
    }

    /**
     * Подготовка фабрики.
     */
    public default void prepare() {
    }

    /**
     * Добавление нового освободившегося ид.
     *
     * @param id освободившийся ид.
     */
    public default void releaseId(final int id) {
    }

    /**
     * @return кол-во использованных ид.
     */
    public default int usedIds() {
        return 0;
    }
}
