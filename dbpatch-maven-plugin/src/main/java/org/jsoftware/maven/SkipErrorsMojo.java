package org.jsoftware.maven;

import org.jsoftware.command.SkipErrorsCommand;
import org.jsoftware.config.EnvSettings;

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
