package org.jsoftware.maven;

import org.jsoftware.command.InteractiveCommand;


/**
 * Runs interactive client from maven
 *
 * @author szalik
 * @goal interactive
 */
public class InteractiveMojo extends CommandMojoAdapter<InteractiveCommand> {

    protected InteractiveMojo() {
        super(new InteractiveCommand());
    }
}
