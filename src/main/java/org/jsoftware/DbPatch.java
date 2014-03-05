package org.jsoftware;

import org.jsoftware.command.*;
import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.InteractivePanel;
import org.jsoftware.log.LogFactory;

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
        LogFactory.initLocal();
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
                System.err.println("Arg 1 must be mojo name, arg 2 can be configuration file.");
                new HelpCommand().execute();
            } else {
                LogFactory.getInstance().debug("Lunching text mode - " + command + ".");
                command.setConfigFile(confFile);
                command.execute();
            }
        }
    }

    private static AbstractCommand argToCommand(String mojoArg) {
        if ("help".equalsIgnoreCase(mojoArg)) {
            return new HelpCommand();
        }
        if ("help-parse".equalsIgnoreCase(mojoArg)) {
            return new HelpParseCommand();
        }
        if ("interactive".equalsIgnoreCase(mojoArg)) {
            return new InteractiveCommand();
        }
        if ("list".equalsIgnoreCase(mojoArg)) {
            return new ListCommand();
        }
        if ("patch".equalsIgnoreCase(mojoArg)) {
            return new PatchCommand();
        }
        if ("skip-errors".equalsIgnoreCase(mojoArg)) {
            return new SkipErrorsCommand();
        }
        return null;
    }

}
