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
import org.jsoftware.impl.DirectoryPatchScaner;
import org.jsoftware.impl.NamePatchScaner;


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
				ce.setApplayStartegy(value);
			}
			if ("extensions".equalsIgnoreCase(keys[1])) {
				ce.setExtensions(value);
			}
			if ("encoding".equalsIgnoreCase(keys[1])) {
				ce.setPatchEncoding(value);
			}
			if ("scaner".equalsIgnoreCase(keys[1])) {
				PatchScaner scaner = null;
				value = value.toLowerCase().trim();
				if (value.startsWith("dir")) scaner = new DirectoryPatchScaner();
				if (value.startsWith("name")) scaner = new NamePatchScaner();
				if (scaner == null) {
					throw new ParseException("Unknow scaner type - " + value, 0);
				} 
				ce.setPatchScaner(scaner);
			}
		}
		return confs.values();
	}

}
