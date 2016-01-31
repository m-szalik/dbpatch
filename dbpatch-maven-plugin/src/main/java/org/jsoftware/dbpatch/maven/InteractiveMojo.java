package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.InteractiveCommand;
import org.jsoftware.dbpatch.config.EnvSettings;

/**
 * Runs interactive client from maven
 *
 * @author szalik
 * @goal interactive
 */
public class InteractiveMojo extends CommandMojoAdapter<InteractiveCommand> {

    protected InteractiveMojo() {
        super(new InteractiveCommand(EnvSettings.maven()));
    }
}
