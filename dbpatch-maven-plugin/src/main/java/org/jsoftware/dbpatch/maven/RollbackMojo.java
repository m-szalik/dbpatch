package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.RollbackCommand;
import org.jsoftware.dbpatch.config.EnvSettings;

/**
 * Roll-back patch or patches
 *
 * @author szalik
 * @goal rollback
 */
public class RollbackMojo extends CommandSingleConfMojoAdapter<RollbackCommand> {


    protected RollbackMojo() {
        super(new RollbackCommand(EnvSettings.maven()));
    }
}
