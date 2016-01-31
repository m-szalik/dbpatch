package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.SkipErrorsCommand;
import org.jsoftware.dbpatch.config.EnvSettings;

/**
 * Mark patches &quot;in progress&quot; as committed.
 *
 * @author szalik
 * @goal skip-errors
 */
public class SkipErrorsMojo extends CommandSingleConfMojoAdapter<SkipErrorsCommand> {

    protected SkipErrorsMojo() {
        super(new SkipErrorsCommand(EnvSettings.maven()));
    }
}
