package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.RollbackListCommand;


/**
 * @author szalik
 */
public class RollbackListTask extends AbstractDbPatchTask<RollbackListCommand> {

    public RollbackListTask() {
        super(CommandFactory.defaultFactory(RollbackListCommand.class), "Checks for missing rollback scripts.");
    }
}
