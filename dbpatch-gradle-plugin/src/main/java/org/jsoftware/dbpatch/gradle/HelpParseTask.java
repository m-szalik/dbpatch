package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.HelpParseCommand;


/**
 * @author szalik
 */
public class HelpParseTask extends AbstractDbPatchTask<HelpParseCommand> {

    public HelpParseTask() {
        super(CommandFactory.defaultFactory(HelpParseCommand.class), "Parses sql file and prints it.");
    }
}
