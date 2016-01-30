package org.jsoftware.dbpatch.maven;

import org.jsoftware.command.AbstractSingleConfDbPatchCommand;


/**
 * @author szalik
 * @execute phase="process-resources"
 */
//see http://maven.apache.org/developers/mojo-api-specification.html
public abstract class CommandSingleConfMojoAdapter<T extends AbstractSingleConfDbPatchCommand> extends CommandMojoAdapter<T> {

    /**
     * Chosen configuration. Use with property &quot;configFile&quot;.
     *
     * @parameter
     */
    private String selectedConfiguration;

    protected CommandSingleConfMojoAdapter(T command) {
        super(command);
    }

    public void setSelectedConfiguration(String selectedConfiguration) {
        this.selectedConfiguration = selectedConfiguration;
    }

    @Override
    protected void setup(AbstractSingleConfDbPatchCommand command) {
        command.setSelectedConfiguration(selectedConfiguration);
    }
}
