package org.jsoftware.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.jsoftware.log.LogFactory;

public abstract class AbstractDbPatchMojo extends AbstractMojo {
	/**
	 * Configuration file
	 * 
	 * @parameter
	 */
	private File configFile;

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
		if (configFile != null)
			return configFile;
		String cfile = System.getProperty("dbpatch.configFile");
		if (cfile != null) {
			return new File(cfile);
		} else {
			return null;
		}
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
