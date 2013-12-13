package org.jsoftware.maven;

import org.jsoftware.command.RollbackListCommand;


/**
 * Show list of rollabck patches
 * @goal rollback-list
 * @author szalik
 */
public class RollbackListMojo extends CommandSingleConfMojoAdapter<RollbackListCommand> {


    protected RollbackListMojo() {
        super(new RollbackListCommand());
    }
}
