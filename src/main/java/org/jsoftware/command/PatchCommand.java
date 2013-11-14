package org.jsoftware.command;

import org.jsoftware.config.Patch;

import java.util.List;


/**
 * Runs auto-patch mode
 * @author szalik
 */
public class PatchCommand extends ListCommand {
    private boolean uptodate;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal() throws Exception {
		List<Patch> patchesToApply = generatePatchList();
		uptodate = false;
		try {
			manager.startExecution();
			for(Patch p : patchesToApply) {
				manager.apply(p);
			}
			uptodate = true;
		} finally {
			manager.endExecution();
		}
	}

    public boolean isUptodate() {
        return uptodate;
    }
}
