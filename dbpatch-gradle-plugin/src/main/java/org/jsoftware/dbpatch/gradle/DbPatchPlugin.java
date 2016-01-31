package org.jsoftware.dbpatch.gradle;


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jsoftware.dbpatch.log.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbPatchPlugin implements Plugin<Project> {
    public DbPatchPlugin() {
        Logger logger = LoggerFactory.getLogger(getClass());
        LogFactory.init(new GradleLog(logger));
    }

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


class GradleLog implements org.jsoftware.dbpatch.log.Log {
    private final Logger logger;

    public GradleLog(Logger logger) {
        this.logger = logger;
    }

    public void trace(String msg, Throwable e) {
        logger.trace(msg, e);
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void fatal(String msg) {
        logger.error(msg);
    }

    public void warn(String msg, Throwable e) {
        logger.warn(msg, e);
    }

    public void fatal(String msg, Throwable e) {
        logger.error(msg, e);
    }
}