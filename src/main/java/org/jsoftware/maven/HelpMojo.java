package org.jsoftware.maven;

import org.jsoftware.command.HelpCommand;



/**
 * Display help
 * @goal help
 * @author szalik
 */
public class HelpMojo extends CommandMojoAdapter<HelpCommand> {

    protected HelpMojo() {
        super(new HelpCommand());
    }
}
