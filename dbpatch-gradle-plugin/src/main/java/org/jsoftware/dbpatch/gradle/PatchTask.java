package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.PatchCommand;


/**
 * Runs auto-patch mode
 *
 * @author szalik
 */
public class PatchTask extends AbstractDbPatchTask<PatchCommand> {

    public PatchTask() {
        super(CommandFactory.defaultFactory(PatchCommand.class), "Apply patches into database.");
    }
}
