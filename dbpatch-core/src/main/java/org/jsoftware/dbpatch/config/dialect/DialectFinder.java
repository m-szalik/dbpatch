package org.jsoftware.dbpatch.config.dialect;

import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

/**
 * It tries to autodetect database Dialect by parsing jdbc driver class name
 */
public class DialectFinder {
    private DialectFinder() {
    }

    public static Dialect find(String value) {
        value = value.trim().toLowerCase();
        if (value.length() == 0 || value.equalsIgnoreCase("default")) {
            return null;
        }
        return createDialect(Character.toUpperCase(value.charAt(0)) + value.substring(1));
    }

    private static Dialect createDialect(String dialectName) {
        String value = "org.jsoftware.config.dialect." + dialectName;
        if (!value.endsWith("Dialect")) {
            value = value + "Dialect";
        }
        try {
            Class<?> cl = Class.forName(value);
            return (Dialect) cl.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can not find class " + value + " for dialect " + dialectName);
        } catch (Exception e) {
            throw new RuntimeException("Can not instance dialect " + value);
        }
    }

    public static Dialect findByDriverClass(String driverClassName) {
        Log log = LogFactory.getInstance();
        log.debug("Try to detect dialect by driver class name '" + driverClassName + "'");
        String cl = driverClassName.toLowerCase();
        if (cl.contains("oracle")) {
            return createDialect("Oracle");
        }
        if (cl.contains("sybase")) {
            return createDialect("Sybase");
        }
        return null;
    }
}
