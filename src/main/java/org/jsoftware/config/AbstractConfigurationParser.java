package org.jsoftware.config;

import org.jsoftware.impl.CloseUtil;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

import java.io.*;
import java.text.ParseException;
import java.util.Collection;

/**
 * Base class for parsing configuration
 */
public abstract class AbstractConfigurationParser {

    public static Collection<ConfigurationEntry> discoverConfiguration(Object confFile) throws ParseException, IOException {
        final Log log = LogFactory.getInstance();
        InputStream input = null;
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
                }
            } else {
                File file = (File) confFile;
                if (!file.exists()) {
                    throw new FileNotFoundException(file.getAbsolutePath());
                }
                log.debug("Configuration found - " + file.getPath());
                input = new FileInputStream(file);
            }
            AbstractConfigurationParser parser = new PropertiesConfigurationParser();
            Collection<ConfigurationEntry> conf = parser.parse(input);
            for (ConfigurationEntry ce : conf) {
                ce.validate();
            }
            return conf;
        } finally {
            CloseUtil.close(input);
        }
    }

    protected abstract Collection<ConfigurationEntry> parse(InputStream input) throws ParseException, IOException;
}
