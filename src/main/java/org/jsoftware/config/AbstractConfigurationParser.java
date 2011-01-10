package org.jsoftware.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;

import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

public abstract class AbstractConfigurationParser {
	
	public static Collection<ConfigurationEntry> discoverConfiguration(Object confFile) throws ParseException, IOException {
		final Log log = LogFactory.getInstance();
		InputStream input;
		if (confFile == null) {
			log.debug("Looking for dbpach.properties in classpath.");
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream("/dbpatch.properties");
			log.debug("Resource dbpatch.properties " +( input == null ? "not" : "" )+ " found in classpath.");
		} else {
			File file = (File) confFile;
			if (! file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
			log.debug("Configuration found - " + file.getPath());
			input = new FileInputStream(file);
		}
		if (input == null) {
			throw new IOException("No configuration file found.");
		}
		AbstractConfigurationParser parser = new PropertiesConfigurationParser();
		Collection<ConfigurationEntry> conf = parser.parse(input);
		for(ConfigurationEntry ce : conf) {
			ce.validate();
		}
		return conf;
	}

	public abstract Collection<ConfigurationEntry> parse(InputStream input) throws ParseException, IOException;
}
