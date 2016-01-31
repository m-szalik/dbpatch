package org.jsoftware.dbpatch.gradle;

import java.io.File;

public class DbPatchConfiguration {
    private String selectedConfiguration;
    private File configFile;
    private File baseDir;

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = new File(configFile);
        if (baseDir == null) {
            baseDir = this.configFile.getParentFile();
        }
    }

    public String getSelectedConfiguration() {
        return selectedConfiguration;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public File getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = new File(baseDir);
    }

    public void setSelectedConfiguration(String selectedConfiguration) {
        this.selectedConfiguration = selectedConfiguration;
    }
}
