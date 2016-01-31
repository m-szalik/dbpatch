package org.jsoftware.dbpatch.gradle;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DbPatchPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("dbpatch", DbPatchConfiguration.class);

        project.getTasks().create("dbpatch-help", HelpTask.class);
        project.getTasks().create("dbpatch-interactive", InteractiveTask.class);
        project.getTasks().create("dbpatch-list", ListTask.class);
        project.getTasks().create("dbpatch-patch", PatchTask.class);
        project.getTasks().create("dbpatch-rollback-list", RollbackListTask.class);
        project.getTasks().create("dbpatch-rollback", RollbackTask.class);
        project.getTasks().create("dbpatch-skip", SkipErrorsTask.class);
    }

}
