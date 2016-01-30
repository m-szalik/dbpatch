package org.jsoftware.dbpatch.maven;

import org.jsoftware.command.InteractiveCommand;
import org.jsoftware.config.EnvSettings;


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
