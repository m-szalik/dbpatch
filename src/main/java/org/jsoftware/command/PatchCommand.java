package org.jsoftware.command;

import org.jsoftware.config.Patch;

import java.util.List;


/**
 * Command: Runs auto-patch mode
 *
 * @author szalik
 */
public class PatchCommand extends ListCommand implements CommandSuccessIndicator {
    private boolean success;

    @SuppressWarnings("unchecked")
    @Override
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


    @Override
    public boolean isSuccess() {
        return success;
    }
}
