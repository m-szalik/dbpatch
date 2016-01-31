package org.jsoftware.dbpatch.maven;


import org.jsoftware.dbpatch.command.ListCommand;
import org.jsoftware.dbpatch.config.EnvSettings;

/**
 * Show list of patches
 *
 * @author szalik
 * @goal list
 */
public class ListMojo extends CommandSingleConfMojoAdapter<ListCommand> {


    protected ListMojo() {
        super(new ListCommand(EnvSettings.maven()));
    }
}
