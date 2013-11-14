package org.jsoftware.command;

import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.log.LogFactory;

import java.io.File;

/**
 * @author szalik
 */
public abstract class AbstractCommand {

    private File configFile;

    private ConfigurationEntry conf;

    protected File directory;

    protected org.jsoftware.log.Log log = LogFactory.getInstance();

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public void setConf(ConfigurationEntry conf) {
        this.conf = conf;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
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


    public abstract void execute() throws CommandExecutionException, CommandFailureException;

}
