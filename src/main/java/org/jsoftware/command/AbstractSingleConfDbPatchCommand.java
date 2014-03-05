package org.jsoftware.command;

import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.ConsoleDbManagerPasswordCallback;
import org.jsoftware.impl.DbManager;
import org.jsoftware.log.LogFactory;

import java.io.File;
import java.util.Collection;


/**
 * @author szalik
 */
public abstract class AbstractSingleConfDbPatchCommand extends AbstractCommand {
	private String selectedConfiguration;
	protected ConfigurationEntry configurationEntry;
	protected DbManager manager;
	
	
	public void setSelectedConfiguration(String selectedConfiguration) {
		this.selectedConfiguration = selectedConfiguration;
	}
	
	public void execute() throws CommandExecutionException, CommandFailureException {
        String configurationProperty = System.getProperty("maven.dbpatch.configuration");
        if (configurationProperty != null && configurationProperty.trim().length() > 0) {
            LogFactory.getInstance().info("Using profile \"" + configurationProperty.trim() + "\".");
            selectedConfiguration =configurationProperty.trim();
        }
        try {
			File cfgFile = getConfigFile();
			ConfigurationEntry mavenConfigurationEntry = getConf();
			if (cfgFile != null && mavenConfigurationEntry != null) {
				throw new CommandExecutionException("Ambiguous configuration. Both \"configFile\" and \"conf\" properties are set!");
			}
			if (mavenConfigurationEntry == null && cfgFile == null) {
				throw new CommandExecutionException("Configuration problem. Set one of \"configFile\" or \"conf\" property!");
			}
			
			if (cfgFile != null) {
				Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(getConfigFile());
				if (selectedConfiguration == null) {
					throw new CommandFailureException(this, "configuration missing - selectedConfiguration not set", "Please set maven.dbpatch.configuration property.");
				}
				ConfigurationEntry confEntry = null;
				for(ConfigurationEntry ce : conf) {
					if (ce.getId().equals(selectedConfiguration)) {
						confEntry = ce; 
						break;
					}
				}
				if (confEntry == null) {
                    throw new CommandFailureException(this, "configuration missing - no configuration for " + selectedConfiguration, "No configuration for " + selectedConfiguration + ".");
                }
				this.configurationEntry = confEntry;
			} 
			
			if (mavenConfigurationEntry != null) {
				mavenConfigurationEntry.validate();
				configurationEntry = mavenConfigurationEntry;
			}
			
			log.debug("Configuration:\n\t" + configurationEntry.toString().replace("\n", "\n\t").trim());
			// do work
			DbManager manager = new DbManager(configurationEntry);
			manager.init(new ConsoleDbManagerPasswordCallback());
			this.manager = manager;
			log.info("Configuration: " + configurationEntry.getId());
			executeInternal();
			manager.dispose();
		} catch (Exception e) {
			throw new CommandExecutionException(e.getMessage(), e);
		}
		
	}
	
    
	protected abstract void executeInternal() throws Exception;

    public String getSelectedConfiguration() {
        return selectedConfiguration;
    }

    public ConfigurationEntry getConfigurationEntry() {
        return configurationEntry;
    }
}
