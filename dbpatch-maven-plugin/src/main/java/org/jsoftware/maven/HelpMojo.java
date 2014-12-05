package org.jsoftware.maven;

import org.jsoftware.command.HelpCommand;


/**
 * Display help
 *
 * @author szalik
 * @goal help
 */
public class HelpMojo extends CommandMojoAdapter<HelpCommand> {

    protected HelpMojo() {
        super(new HelpCommand());
    }
}
