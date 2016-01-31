package org.jsoftware.dbpatch.gradle;

import org.jsoftware.dbpatch.command.AbstractCommand;
import org.jsoftware.dbpatch.command.HelpCommand;
import org.jsoftware.dbpatch.config.EnvSettings;


/**
 * @author szalik
 */
public class HelpTask extends AbstractDbPatchTask<HelpCommand> {

    public HelpTask() {
        super(new CommandFactory() {
            @Override
            AbstractCommand getCommand() {
                return new HelpCommand(EnvSettings.gradle(), "dbpatch-", "gradle.");
            }
        }, "Displays a help message.");
    }
}
