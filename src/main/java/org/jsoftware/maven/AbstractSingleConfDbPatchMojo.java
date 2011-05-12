package org.jsoftware.maven;

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.DbManager;


/**
 * 
 * @author mgruszecki
 * @execute phase="process-resources"
 */
//see http://maven.apache.org/developers/mojo-api-specification.html
public abstract class AbstractSingleConfDbPatchMojo extends AbstractDbPatchMojo {
	
	/**
	 * Choosen configuration. Use with property &quot;configFile&quot;.
	 * @parameter 
	 */
	private String selectedConfiguration;
	protected ConfigurationEntry configurationEntry;
	protected DbManager manager;
	
	
	public void setSelectedConfiguration(String selectedConfiguration) {
		this.selectedConfiguration = selectedConfiguration;
	}
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (System.getProperty("maven.dbpatch.skip") != null) {
			log.debug("dbpatch skiped");
			return;
		}
		try {
			File cfgFile = getConfigFile();
			ConfigurationEntry mavenConfigurationEntry = getConf();
			if (cfgFile != null && mavenConfigurationEntry != null) {
				throw new MojoExecutionException("Ambiguous configuration. Both \"configFile\" and \"conf\" properties are set!");
			}
			if (mavenConfigurationEntry == null && cfgFile == null) {
				throw new MojoExecutionException("Configuration problem. Set one of \"configFile\" or \"conf\" property!");
			}
			
			if (cfgFile != null) {
				Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(getConfigFile());
				if (selectedConfiguration == null) {
					selectedConfiguration = System.getProperty("maven.dbpatch.configuration");
				}
				if (selectedConfiguration == null) {
					throw new MojoFailureException(this, "configuration missing - selectedConfiguration not set", "Please set maven.dbpatch.configuration property.");
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
			} 
			
			if (mavenConfigurationEntry != null) {
				mavenConfigurationEntry.validate();
				configurationEntry = mavenConfigurationEntry;
			}
			
			log.debug("Configuration:\n\t" + configurationEntry.toString().replace("\n", "\n\t").trim());
			// do work
			DbManager manager = new DbManager(configurationEntry);
			this.manager = manager;
			log.info("Configuration: " + configurationEntry.getId());
			executeInternal();
			manager.dispose();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		
	}
	
    
	protected abstract void executeInternal() throws Exception;
}
