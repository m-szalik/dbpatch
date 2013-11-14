package org.jsoftware.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.log.LogFactory;

import java.io.File;

/**
 * Abstract mojo for dbPatch plugin goals
 * @author szalik
 */
public abstract class AbstractDbPatchMojo extends AbstractMojo {
	
	/**
	 * Configuration file
	 * @parameter
	 */
	private File configFile;
	
	/**
	 * Daatabase and patch configuration
	 * @see ConfigurationEntry's fields 
	 * @parameter 
	 */
	private ConfigurationEntry conf;

	/**
	 * Project directory
	 * @parameter default-value="${basedir}"
	 * @required
	 * @readonly
	 */
	protected File directory;

	protected org.jsoftware.log.Log log = LogFactory.getInstance();

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public File getConfigFile() {
		if (configFile != null) {
			return configFile;
        }
		String cFile = System.getProperty("dbpatch.configFile");
		if (cFile != null) {
			return new File(cFile);
		} else {
			return null;
		}
	}
	
	public ConfigurationEntry getConf() {
		return conf;	/* firebug ok - unwritten field (maven writes it) */
	}

	@Override
	public void setLog(Log log) {
		super.setLog(log);
		if (log != null) {
			LogFactory.initMaven(log);
			this.log = LogFactory.getInstance();
		}
	}

}
