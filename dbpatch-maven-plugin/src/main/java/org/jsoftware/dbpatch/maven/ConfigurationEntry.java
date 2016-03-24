package org.jsoftware.dbpatch.maven;

import java.io.File;

/**
 */
public class ConfigurationEntry extends org.jsoftware.dbpatch.config.ConfigurationEntry {

    public ConfigurationEntry() {
        super("pom.xml", new File("."));
    }

}
