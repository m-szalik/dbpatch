package org.jsoftware.config;

import org.jsoftware.config.dialect.DefaultDialect;
import org.jsoftware.config.dialect.DialectFinder;
import org.jsoftware.impl.DirectoryPatchScanner;
import org.jsoftware.impl.NamePatchScanner;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertiesConfigurationParser extends AbstractConfigurationParser {

	@Override
	public Collection<ConfigurationEntry> parse(InputStream input) throws IOException, ParseException {
		Properties p = new Properties();
		p.load(input);
		Map<String,ConfigurationEntry> confs = new HashMap<String,ConfigurationEntry>();
		for(Map.Entry<Object,Object> me : p.entrySet()) {
			String[] keys = me.getKey().toString().toLowerCase().split("\\.");
			if (keys.length != 2) throw new ParseException("Invalid key " + me.getKey(), 0);
			ConfigurationEntry ce = confs.get(keys[0]);
			if (ce == null) {
				ce = new ConfigurationEntry(keys[0]);
				ce.setDialectInstance(new DefaultDialect());
				confs.put(keys[0], ce);
			}
			String value = me.getValue().toString();
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
				ce.setDialectInstance(DialectFinder.find(value));
			}
			if ("dirs".equalsIgnoreCase(keys[1]) || "patchDirs".equalsIgnoreCase(keys[1])) {
				ce.setPatchDirs(value);
			}
			if ("driverClass".equalsIgnoreCase(keys[1]) || "driver".equalsIgnoreCase(keys[1])) {
				ce.setDriverClass(value);
			}
			if ("strategy".equalsIgnoreCase(keys[1]) || "applyStrategy".equalsIgnoreCase(keys[1])) {
				ce.setApplyStarters(value);
			}
			if ("extensions".equalsIgnoreCase(keys[1])) {
				ce.setExtensions(value);
			}
			if ("encoding".equalsIgnoreCase(keys[1])) {
				ce.setPatchEncoding(value);
			}
			if ("scanner".equalsIgnoreCase(keys[1])) {
				PatchScanner scanner = null;
				value = value.toLowerCase().trim();
				if (value.startsWith("dir")) scanner = new DirectoryPatchScanner();
				if (value.startsWith("name")) scanner = new NamePatchScanner();
				if (scanner == null) {
					throw new ParseException("Unknow scanner type - " + value, 0);
				} 
				ce.setPatchScanner(scanner);
			}
		}
		return confs.values();
	}

}
