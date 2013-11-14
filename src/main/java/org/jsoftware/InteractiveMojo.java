package org.jsoftware;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.InteractivePanel;
import org.jsoftware.maven.AbstractDbPatchMojo;

import java.util.Collection;


/**
 * Runs interactive client from maven
 * @goal interactive
 * @author szalik
 */
public class InteractiveMojo extends AbstractDbPatchMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(getConfigFile());
			InteractivePanel interactive = new InteractivePanel(conf);
			interactive.start();
			interactive.join();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
    
}
