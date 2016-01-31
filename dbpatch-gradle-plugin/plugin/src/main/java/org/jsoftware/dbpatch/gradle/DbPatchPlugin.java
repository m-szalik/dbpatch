package org.jsoftware.dbpatch.gradle;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DbPatchPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("dbpatch", DbPatchExtension.class);
        project.getTasks().create("list", ListTask.class);
    }

}
