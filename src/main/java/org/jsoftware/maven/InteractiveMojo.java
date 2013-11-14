package org.jsoftware.maven;

import org.jsoftware.command.InteractiveCommand;


/**
 * Runs interactive client from maven
 * @goal interactive
 * @author szalik
 */
public class InteractiveMojo extends CommandMojoAdapter<InteractiveCommand> {

    protected InteractiveMojo(InteractiveCommand command) {
        super(command);
    }
}
