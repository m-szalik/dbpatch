package org.jsoftware.dbpatch.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class ListTask extends DefaultTask {

    @TaskAction
    public void list() {
        DbPatchConfigurationEntryExtension extension = getProject().getExtensions().getByType(DbPatchConfigurationEntryExtension.class);
        System.out.println("List......" + extension + " > " + extension.getConfigurationEntry());
    }

}
