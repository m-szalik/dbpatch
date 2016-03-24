package org.jsoftware.dbpatch.config;

import org.jsoftware.dbpatch.impl.CloseUtil;
import org.jsoftware.dbpatch.log.Log;
import org.jsoftware.dbpatch.log.LogFactory;

import java.io.*;
import java.text.ParseException;
import java.util.Collection;

/**
 * Base class for parsing configuration
 */
public abstract class AbstractConfigurationParser {

    public static Collection<ConfigurationEntry> discoverConfiguration(File confFile) throws ParseException, IOException {
        final Log log = LogFactory.getInstance();
        InputStream input = null;
        File baseDir = null;
        try {
            if (confFile == null) {
                log.debug("Looking for dbpach.properties in classpath.");
                input = Thread.currentThread().getContextClassLoader().getResourceAsStream("/dbpatch.properties");
                log.debug("Resource dbpatch.properties " + (input == null ? "not" : "") + " found in classpath.");
                if (input == null) {
                    log.debug("Looking for dbpach.properties in current directory.");
                    File f = new File("dbpatch.properties");
                    log.debug("Resource dbpatch.properties " + (!f.exists() ? "not" : "") + " found in current directory.");
                    input = new FileInputStream(f);
                    baseDir = new File(".");
                }
            } else {
                if (!confFile.exists()) {
                    throw new FileNotFoundException(confFile.getAbsolutePath());
                }
                log.debug("Configuration found - " + confFile.getPath());
                input = new FileInputStream(confFile);
                baseDir = confFile.getParentFile();
            }
            AbstractConfigurationParser parser = new PropertiesConfigurationParser();
            Collection<ConfigurationEntry> conf = parser.parse(baseDir, input);
            for (ConfigurationEntry ce : conf) {
                ce.validate();
            }
            return conf;
        } finally {
            CloseUtil.close(input);
        }
    }

    protected abstract Collection<ConfigurationEntry> parse(File baseDir, InputStream input) throws ParseException, IOException;
}
