package org.jsoftware.dbpatch.gradle;

import java.io.File;

public class DbPatchConfiguration {
    private String selectedConfiguration;
    private File configFile;

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = new File(configFile);
    }

    public String getSelectedConfiguration() {
        return selectedConfiguration;
    }

    public void setSelectedConfiguration(String selectedConfiguration) {
        this.selectedConfiguration = selectedConfiguration;
    }
}
