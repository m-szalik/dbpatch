package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.RollbackCommand;


/**
 * @author szalik
 */
public class RollbackTask extends AbstractDbPatchTask<RollbackCommand> {

    public RollbackTask() {
        super(CommandFactory.defaultFactory(RollbackCommand.class), "Rollbacks script or scripts.");
    }
}
