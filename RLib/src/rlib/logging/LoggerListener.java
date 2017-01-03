package rlib.logging;

/**
 * The listener for listening logger events.
 *
 * @author JavaSaBr
 */
public interface LoggerListener {

    /**
     * @param text the new text.
     */
    void println(String text);

    /**
     * Flushes last data.
     */
    default void flush() {
    }
}
