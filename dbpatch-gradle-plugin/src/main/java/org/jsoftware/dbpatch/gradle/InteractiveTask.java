package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.InteractiveCommand;


/**
 * @author szalik
 */
public class InteractiveTask extends AbstractDbPatchTask<InteractiveCommand> {

    public InteractiveTask() {
        super(CommandFactory.defaultFactory(InteractiveCommand.class), "Starts interactive mode.");
    }
}
