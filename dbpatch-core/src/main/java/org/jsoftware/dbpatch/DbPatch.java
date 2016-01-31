package org.jsoftware.dbpatch;

import org.jsoftware.dbpatch.command.AbstractCommand;
import org.jsoftware.dbpatch.command.CommandExecutionException;
import org.jsoftware.dbpatch.command.CommandFailureException;
import org.jsoftware.dbpatch.command.HelpCommand;
import org.jsoftware.dbpatch.command.HelpParseCommand;
import org.jsoftware.dbpatch.command.InteractiveCommand;
import org.jsoftware.dbpatch.command.ListCommand;
import org.jsoftware.dbpatch.command.PatchCommand;
import org.jsoftware.dbpatch.command.SkipErrorsCommand;
import org.jsoftware.dbpatch.config.AbstractConfigurationParser;
import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.impl.InteractivePanel;
import org.jsoftware.dbpatch.log.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

/**
 * Standalone application with command-line and GUI support
 *
 * @author szalik
 */
public class DbPatch {

    public static void main(String[] args) throws ParseException, IOException, CommandFailureException, CommandExecutionException {
        EnvSettings envSettings = EnvSettings.standalone();
        LogFactory.initLocal(envSettings);
        File confFile;

        if (args.length > 1) {
            confFile = new File(args[1]);
            if (!confFile.exists()) {
                throw new FileNotFoundException("Cannot find configuration file - " + confFile.getAbsolutePath());
            }
        } else {
            confFile = null;
        }
        if (args.length == 0) { // interactive mode
            LogFactory.getInstance().debug("Lunching interactive mode.");
            Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(confFile);
            InteractivePanel interactive = new InteractivePanel(conf);
            interactive.start();
        } else { // mojo (text) mode
            AbstractCommand command = argToCommand(args[0]);
            if (command == null) {
                System.err.println("Arg 1 must be command name, arg 2 can be configuration file.");
                new HelpCommand(envSettings).execute();
            } else {
                LogFactory.getInstance().debug("Lunching text mode - " + command + ".");
                command.setConfigFile(confFile);
                command.execute();
            }
        }
    }

    private static AbstractCommand argToCommand(String mojoArg) {
        EnvSettings envSettings = EnvSettings.standalone();
        if ("help".equalsIgnoreCase(mojoArg)) {
            return new HelpCommand(envSettings);
        }
        if ("help-parse".equalsIgnoreCase(mojoArg)) {
            return new HelpParseCommand(envSettings);
        }
        if ("interactive".equalsIgnoreCase(mojoArg)) {
            return new InteractiveCommand(envSettings);
        }
        if ("list".equalsIgnoreCase(mojoArg)) {
            return new ListCommand(envSettings);
        }
        if ("patch".equalsIgnoreCase(mojoArg)) {
            return new PatchCommand(envSettings);
        }
        if ("skip-errors".equalsIgnoreCase(mojoArg)) {
            return new SkipErrorsCommand(envSettings);
        }
        return null;
    }

}
