package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.HelpCommand;
import org.jsoftware.dbpatch.config.EnvSettings;

/**
 * Display help
 *
 * @author szalik
 * @goal help
 */
public class HelpMojo extends CommandMojoAdapter<HelpCommand> {

    protected HelpMojo() {
        super(new HelpCommand(EnvSettings.maven(), "dbpatch:", "maven."));
    }
}
