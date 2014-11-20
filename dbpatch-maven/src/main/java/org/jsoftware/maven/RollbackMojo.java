package org.jsoftware.maven;

import org.jsoftware.command.RollbackCommand;


/**
 * Roll-back patch or patches
 *
 * @author szalik
 * @goal rollback
 */
public class RollbackMojo extends CommandSingleConfMojoAdapter<RollbackCommand> {


    protected RollbackMojo() {
        super(new RollbackCommand());
    }
}
