package org.jsoftware.dbpatch.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.jsoftware.dbpatch.command.AbstractCommand;
import org.jsoftware.dbpatch.command.AbstractSingleConfDbPatchCommand;
import org.jsoftware.dbpatch.command.CommandExecutionException;
import org.jsoftware.dbpatch.command.CommandFailureException;
import org.jsoftware.dbpatch.config.EnvSettings;

import java.io.File;
import java.lang.reflect.Constructor;

abstract class AbstractDbPatchTask<C extends AbstractCommand> extends DefaultTask {
    private final CommandFactory<C> commandFactory;

    protected AbstractDbPatchTask(CommandFactory<C> commandFactory, String description) {
        this.commandFactory = commandFactory;
        setGroup("Database management tasks");
        setDescription(description);
    }

    @TaskAction
    public void action() throws CommandExecutionException, CommandFailureException {
        DbPatchConfiguration dbpatchExt = getProject().getExtensions().getByType(DbPatchConfiguration.class);
        if (dbpatchExt == null) {
            throw new IllegalStateException("Cannot obtain dbpatch configuration.");
        }
        C command = commandFactory.getCommand();
        command.setConfigFile(dbpatchExt.getConfigFile());
        File baseDir = dbpatchExt.getBaseDir();
        if (baseDir == null) {
            baseDir = getProject().getProjectDir();
        }
        command.setDirectory(baseDir);
        if (command instanceof AbstractSingleConfDbPatchCommand) {
            AbstractSingleConfDbPatchCommand singleConfDbPatchCommand = (AbstractSingleConfDbPatchCommand) command;
            singleConfDbPatchCommand.setSelectedConfiguration(dbpatchExt.getSelectedConfiguration());
        }
        command.execute();
    }
}


abstract class CommandFactory<C extends AbstractCommand> {
    abstract C getCommand();

    static <C extends AbstractCommand> CommandFactory<C> defaultFactory(final Class<C> clazz) {
        return new CommandFactory<C>() {

            C getCommand() {
                try {
                    Constructor<C> constructor = clazz.getConstructor(EnvSettings.class);
                    return constructor.newInstance(EnvSettings.gradle());
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}