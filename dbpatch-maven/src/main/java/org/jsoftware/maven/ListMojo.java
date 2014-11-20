package org.jsoftware.maven;

import org.jsoftware.command.ListCommand;


/**
 * Show list of patches
 *
 * @author szalik
 * @goal list
 */
public class ListMojo extends CommandSingleConfMojoAdapter<ListCommand> {


    protected ListMojo() {
        super(new ListCommand());
    }
}
