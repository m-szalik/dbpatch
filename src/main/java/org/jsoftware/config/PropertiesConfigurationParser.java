package org.jsoftware.config;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jsoftware.config.dialect.DefaultDialect;
import org.jsoftware.config.dialect.DialectFinder;


public class PropertiesConfigurationParser extends AbstractConfigurationParser {

	@Override
	public Collection<ConfigurationEntry> parse(InputStream input) throws IOException, ParseException {
		Properties p = new Properties();
		p.load(input);
		Map<String,ConfigurationEntry> confs = new HashMap<String,ConfigurationEntry>();
		for(Object key1 : p.keySet()) {
			String[] keys = key1.toString().toLowerCase().split("\\.");
			if (keys.length != 2) throw new ParseException("Invalid key " + key1, 0);
			ConfigurationEntry ce = confs.get(keys[0]);
			if (ce == null) {
				ce = new ConfigurationEntry(keys[0]);
				ce.setDialect(new DefaultDialect());
				confs.put(keys[0], ce);
			}
			String value = p.get(key1).toString();
			if ("jdbcUrl".equalsIgnoreCase(keys[1]) || "jdbcUri".equalsIgnoreCase(keys[1])) {
				ce.setJdbcUri(value);
			}
			if ("username".equalsIgnoreCase(keys[1]) || "user".equalsIgnoreCase(keys[1])) {
				ce.setUser(value);
			}
			if ("password".equalsIgnoreCase(keys[1])) {
				ce.setPassword(value);
			}
			if ("dialect".equalsIgnoreCase(keys[1])) {
				ce.setDialect(DialectFinder.find(value));
			}
			if ("dirs".equalsIgnoreCase(keys[1]) || "patchDirs".equalsIgnoreCase(keys[1])) {
				ce.setPatchDirs(value);
			}
			if ("driverClass".equalsIgnoreCase(keys[1]) || "driver".equalsIgnoreCase(keys[1])) {
				ce.setDriverClass(value);
			}
			if ("strategy".equalsIgnoreCase(keys[1]) || "applyStrategy".equalsIgnoreCase(keys[1])) {
				ce.setApplayStartegy(value);
			}
			if ("extensions".equalsIgnoreCase(keys[1])) {
				ce.setExtensions(value);
			}
		}
		return confs.values();
	}

}
