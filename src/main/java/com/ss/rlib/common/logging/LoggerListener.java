package com.ss.rlib.common.logging;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a listener of logger events.
 *
 * @author JavaSaBr
 */
public interface LoggerListener {

    /**
     * Print the result logger message.
     *
     * @param text the text.
     */
    void println(@NotNull String text);

    /**
     * Flush last data.
     */
    default void flush() {
    }
}
