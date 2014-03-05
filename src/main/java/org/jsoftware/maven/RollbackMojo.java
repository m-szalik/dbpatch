package org.jsoftware.maven;

import org.jsoftware.command.RollbackCommand;


/**
 * Rollabck patch or patches
 *
 * @author szalik
 * @goal rollback
 */
public class RollbackMojo extends CommandSingleConfMojoAdapter<RollbackCommand> {


    protected RollbackMojo() {
        super(new RollbackCommand());
    }
}
