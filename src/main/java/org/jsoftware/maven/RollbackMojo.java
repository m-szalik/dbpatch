package org.jsoftware.maven;

import org.jsoftware.command.RollbackCommand;


/**
 * Rollabck patch or patches
 * @goal rollback
 * @author szalik
 */
public class RollbackMojo extends CommandSingleConfMojoAdapter<RollbackCommand> {


    protected RollbackMojo() {
        super(new RollbackCommand());
    }
}
