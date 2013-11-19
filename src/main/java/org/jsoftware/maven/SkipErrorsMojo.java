package org.jsoftware.maven;

import org.jsoftware.command.SkipErrorsCommand;

/**
 * Mark patches &quot;in progress&quot; as committed.
 * @goal skip-errors
 * @author szalik
 */
public class SkipErrorsMojo extends CommandSingleConfMojoAdapter<SkipErrorsCommand> {

    protected SkipErrorsMojo() {
        super(new SkipErrorsCommand());
    }
}
