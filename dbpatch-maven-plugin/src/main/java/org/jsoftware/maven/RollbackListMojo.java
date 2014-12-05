package org.jsoftware.maven;

import org.jsoftware.command.RollbackListCommand;


/**
 * Show list of roll-back patches
 *
 * @author szalik
 * @goal rollback-list
 */
public class RollbackListMojo extends CommandSingleConfMojoAdapter<RollbackListCommand> {


    protected RollbackListMojo() {
        super(new RollbackListCommand());
    }
}
