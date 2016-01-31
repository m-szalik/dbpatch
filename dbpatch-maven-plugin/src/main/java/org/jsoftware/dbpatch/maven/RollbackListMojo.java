package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.RollbackListCommand;
import org.jsoftware.dbpatch.config.EnvSettings;

/**
 * Show list of roll-back patches
 *
 * @author szalik
 * @goal rollback-list
 */
public class RollbackListMojo extends CommandSingleConfMojoAdapter<RollbackListCommand> {


    protected RollbackListMojo() {
        super(new RollbackListCommand(EnvSettings.maven()));
    }
}
