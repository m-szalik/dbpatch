package org.jsoftware.command;

import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.InteractivePanel;

import java.util.Collection;


/**
 * Command: Runs interactive client from maven
 * @author szalik
 */
public class InteractiveCommand extends AbstractCommand {

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
