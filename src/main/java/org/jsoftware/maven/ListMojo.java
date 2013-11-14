package org.jsoftware.maven;

import org.jsoftware.command.ListCommand;


/**
 * Show list of patches
 * @goal list
 * @author szalik
 */
public class ListMojo extends CommandSingleConfMojoAdapter<ListCommand> {


    protected ListMojo(ListCommand command) {
        super(command);
    }
}
