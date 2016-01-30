package org.jsoftware.dbpatch.config;

/**
 * Environment. System properties keys definition
 * @author szalik
 */
public class EnvSettings {
    private final String prefix;

    private EnvSettings(String prefix) {
        this.prefix = prefix;
    }

    public String getDbPatchFile() {
        return prefix + "dbpatch.file";
    }

    public String getDbPatchConfiguration() {
        return prefix + "dbpatch.configuration";
    }

    public String getDbPatchStop() {
        return prefix + "dbpatch.stop";
    }

    public String getDbPatchSingle() {
        return prefix + "dbpatch.single";
    }

    public String getLogLevel() {
        return prefix + "dbpatch.logLevel";
    }

    public static EnvSettings standalone() {
        return new EnvSettings("");
    }

    public static EnvSettings maven() {
        return new EnvSettings("maven.");
    }

    public static EnvSettings gradle() {
        return new EnvSettings("gradle.");
    }

}
