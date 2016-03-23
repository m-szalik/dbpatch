package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.HelpCommand;


/**
 * @author szalik
 */
public class HelpTask extends AbstractDbPatchTask<HelpCommand> {

    public HelpTask() {
        super(new CommandFactory<HelpCommand>() {
            HelpCommand getCommand() {
                return HelpCommand.helpCommandGradle();
            }
        }, "Displays a help message.");
    }
}
