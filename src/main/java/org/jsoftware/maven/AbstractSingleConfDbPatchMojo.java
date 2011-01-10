package org.jsoftware.maven;

import java.util.Collection;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.DbManager;



public abstract class AbstractSingleConfDbPatchMojo extends AbstractDbPatchMojo {
	/**
	 * Choosen configuration
	 * @parameter 
	 */
	private String selectedConfiguration;
	protected ConfigurationEntry configurationEntry;
	protected DbManager manager;
	
	
	public void setSelectedConfiguration(String selectedConfiguration) {
		this.selectedConfiguration = selectedConfiguration;
	}
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(getConfigFile());
			if (selectedConfiguration == null) {
				selectedConfiguration = System.getProperty("dbpatch.selectedConfiguration");
			}
			if (selectedConfiguration == null) {
				throw new MojoFailureException(this, "configuration missing - selectedConfiguration not set", "Please set selectedConfiguration property.");
			}
			ConfigurationEntry confEntry = null;
			for(ConfigurationEntry ce : conf) {
				if (ce.getId().equals(selectedConfiguration)) {
					confEntry = ce; 
					break;
				}
			}
			if (confEntry == null) throw new MojoFailureException(this, "configuration missing - no configuration for " + selectedConfiguration, "No configuration for " + selectedConfiguration + ".");
			this.configurationEntry = confEntry;
			// do work
			DbManager manager = new DbManager(confEntry);
			this.manager = manager;
			log.info("Configuration: " + selectedConfiguration);
			executeInternal();
			manager.dispose();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		
	}
    
	protected abstract void executeInternal() throws Exception;
}
