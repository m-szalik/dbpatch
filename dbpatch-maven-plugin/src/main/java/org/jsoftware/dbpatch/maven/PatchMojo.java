package org.jsoftware.dbpatch.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.dbpatch.command.PatchCommand;
import org.jsoftware.dbpatch.config.EnvSettings;


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


    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            super.execute();
        } finally {
            getPluginContext().put(getClass().getName() + "-update", command.isSuccess());
        }
    }
}
