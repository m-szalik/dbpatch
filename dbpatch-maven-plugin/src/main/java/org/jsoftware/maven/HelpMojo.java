package org.jsoftware.maven;

import org.jsoftware.command.HelpCommand;
import org.jsoftware.config.EnvSettings;


/**
 * Display help
 *
 * @author szalik
 * @goal help
 */
public class HelpMojo extends CommandMojoAdapter<HelpCommand> {

    protected HelpMojo() {
        super(new HelpCommand(EnvSettings.maven()));
    }
}
