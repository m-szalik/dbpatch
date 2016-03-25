package org.jsoftware.dbpatch.config;

import org.jsoftware.dbpatch.config.dialect.DialectFinder;
import org.jsoftware.dbpatch.impl.DirectoryPatchScanner;
import org.jsoftware.dbpatch.impl.NamePatchScanner;
import org.jsoftware.dbpatch.impl.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse dbpatch.properties file
 */
public class PropertiesConfigurationParser extends AbstractConfigurationParser {


    public Collection<ConfigurationEntry> parse(File baseDir, InputStream input) throws IOException, ParseException {
        Properties p = new Properties();
        p.load(input);
        evaluatePropertyValuesInternal(p);
        Map<String, ConfigurationEntry> configs = new HashMap<String, ConfigurationEntry>();
        for (Map.Entry<Object, Object> me : p.entrySet()) {
            String[] keys = me.getKey().toString().toLowerCase().split("\\.");
            if (keys.length != 2) {
                throw new ParseException("Invalid key " + me.getKey(), 0);
            }
            ConfigurationEntry ce = configs.get(keys[0]);
            if (ce == null) {
                ce = new ConfigurationEntry(keys[0], baseDir);
                configs.put(keys[0], ce);
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
            if ("rollbackDirs".equalsIgnoreCase(keys[1]) || "undoDirs".equalsIgnoreCase(keys[1])) {
                ce.setRollbackDirs(value);
            }
            if ("rollbackSuffix".equalsIgnoreCase(keys[1])) {
                ce.setRollbackSuffix(value);
            }
            if ("interactivePasswordAllowed".equalsIgnoreCase(keys[1])) {
                if (("true".equalsIgnoreCase(value.trim())) || ("false".equalsIgnoreCase(value.trim()))) {
                    boolean bool = Boolean.valueOf(value.trim());
                    ce.setInteractivePasswordAllowed(bool);
                } else {
                    throw new IllegalArgumentException("Illegal value for property 'interactivePasswordAllowed'. Allowed values 'true' or 'false'.");
                }
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
                if (value.startsWith("dir")) {
                    scanner = new DirectoryPatchScanner();
                }
                if (value.startsWith("name")) {
                    scanner = new NamePatchScanner();
                }
                if (scanner == null) {
                    throw new ParseException("Unknown scanner type - " + value, 0);
                }
                ce.setPatchScanner(scanner);
            }
        }
        return configs.values();
    }


    /**
     * Evaluate strings like ${systemProperty} or ${envName}
     */
    static int evaluatePropertyValuesInternal(Properties props) {
        final Pattern pattern = Pattern.compile("(#|\\$)\\{[^\\}]*\\}");
        int replaceCount = 0;
        // validation
        for (Map.Entry<Object, Object> me : props.entrySet()) {
            if (me.getKey() == null || me.getValue() == null) {
                continue;
            }
            String input = me.getValue().toString();
            Matcher matcher = pattern.matcher(input);
            String result = input;
            int offset = 0;
            while (matcher.find(offset)) {
                offset = matcher.start() + 1;
                String group = matcher.group();
                String replacement = group.substring(2, group.length() - 1);
                if (replacement.length() > 0) {
                    String nv = System.getProperty(replacement);
                    if (nv == null) {
                        nv = System.getenv(replacement);
                    }
                    if (nv == null) {
                        nv = props.getProperty(replacement);
                    }
                    if (nv != null) {
                        replacement = nv;
                    } else {
                        throw new IllegalArgumentException("Cannot resolve configuration expression '" + replacement + "' for key '" + me.getKey() + "'");
                    }
                }
                if (!group.equals(replacement)) {
                    result = StringUtils.replace(result, group, replacement);
                    matcher = pattern.matcher(result);
                    replaceCount++;
                    offset = offset + replacement.length() - 1;
                }
            }
            me.setValue(result);
        } // for
        return replaceCount;
    }
}
