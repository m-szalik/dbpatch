package org.jsoftware.command;

/**
 * @author szalik
 *         It is mapped to MojoFailureException
 *         <p>I got my own exception because this software can be used without maven.</p>
 */
public class CommandFailureException extends Exception {
    private final Object source;
    private final String messageString, descriptionString;

    public CommandFailureException(Object source, String message, String description) {
        super(message + " - " + description);
        this.source = source;
        this.messageString = message;
        this.descriptionString = description;
    }

    public CommandFailureException(String message) {
        super(message);
        this.messageString = message;
        this.source = null;
        this.descriptionString = null;
    }

    public Object getSource() {
        return source;
    }

    public String getMessageString() {
        return messageString;
    }

    public String getDescriptionString() {
        return descriptionString;
    }
}
