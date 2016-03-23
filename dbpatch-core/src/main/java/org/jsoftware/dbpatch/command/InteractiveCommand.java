package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.AbstractConfigurationParser;
import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.impl.gui.InteractivePanel;

import java.util.Collection;


/**
 * Command: Runs interactive client from maven
 *
 * @author szalik
 */
public class InteractiveCommand extends AbstractCommand {

    public InteractiveCommand(EnvSettings envSettings) {
        super(envSettings);
    }

    public void execute() throws CommandExecutionException, CommandFailureException {
        try {
            Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(getConfigFile());
            InteractivePanel interactive = new InteractivePanel(conf);
            interactive.start();
            interactive.join();
        } catch (Exception e) {
            throw new CommandExecutionException(e.getMessage(), e);
        }
    }

}
