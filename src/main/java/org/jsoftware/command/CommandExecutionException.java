package org.jsoftware.command;

/**
 * @author szalik
 *         It is mapped to MojoExecutionException but
 *         <p>I got my own exception because this software can be used without maven.</p>
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
