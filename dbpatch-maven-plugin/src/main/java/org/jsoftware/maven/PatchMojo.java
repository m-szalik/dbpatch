package org.jsoftware.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.command.PatchCommand;
import org.jsoftware.config.EnvSettings;


/**
 * Runs auto-patch mode
 *
 * @author szalik
 * @goal patch
 */
public class PatchMojo extends CommandSingleConfMojoAdapter<PatchCommand> {

    protected PatchMojo() {
        super(new PatchCommand(EnvSettings.maven()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            super.execute();
        } finally {
            getPluginContext().put(getClass().getName() + "-uptodate", command.isSuccess());
        }
    }
}
