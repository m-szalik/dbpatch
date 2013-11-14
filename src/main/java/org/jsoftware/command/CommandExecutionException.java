package org.jsoftware.command;

/**
 * @author szalik
 */
public class CommandExecutionException extends Exception {
    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Throwable init) {
        super(message);
        initCause(init);
    }
}
