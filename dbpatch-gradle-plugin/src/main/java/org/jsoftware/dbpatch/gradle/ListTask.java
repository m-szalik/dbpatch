package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.ListCommand;

/**
 */
public class ListTask extends AbstractDbPatchTask<ListCommand> {

    public ListTask() {
        super(CommandFactory.defaultFactory(ListCommand.class), "Prints a list of patches and their's statuses.");
    }

}
