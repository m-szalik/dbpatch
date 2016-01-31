package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.config.Patch;

import java.util.List;


/**
 * Command: Runs auto-patch mode
 *
 * @author szalik
 */
public class PatchCommand extends ListCommand implements CommandSuccessIndicator {
    private boolean success;

    public PatchCommand(EnvSettings envSettings) {
        super(envSettings);
    }

    @SuppressWarnings("unchecked")

    protected void executeInternal() throws Exception {
        List<Patch> patchesToApply = getList();
        success = false;
        try {
            manager.startExecution();
            for (Patch p : patchesToApply) {
                manager.apply(p);
            }
            success = true;
        } finally {
            manager.endExecution();
        }
    }



    public boolean isSuccess() {
        return success;
    }
}
