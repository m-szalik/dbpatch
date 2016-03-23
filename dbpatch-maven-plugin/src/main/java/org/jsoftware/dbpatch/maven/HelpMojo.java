package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.HelpCommand;

/**
 * Display help
 *
 * @author szalik
 * @goal help
 */
public class HelpMojo extends CommandMojoAdapter<HelpCommand> {

    protected HelpMojo() {
        super(HelpCommand.helpCommandMaven());
    }
}
