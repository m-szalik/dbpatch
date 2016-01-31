package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.SkipErrorsCommand;


/**
 * @author szalik
 */
public class SkipErrorsTask extends AbstractDbPatchTask<SkipErrorsCommand> {

    public SkipErrorsTask() {
        super(CommandFactory.defaultFactory(SkipErrorsCommand.class), "Marks patches \"in progress\" as committed.");
    }
}
