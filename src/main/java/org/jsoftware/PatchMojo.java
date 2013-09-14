package org.jsoftware;

import org.jsoftware.config.Patch;

import java.util.List;


/**
 * Runs auto-patch mode
 * @goal patch
 */
public class PatchMojo extends ListMojo {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal() throws Exception {
		List<Patch> patchesToApply = generatePatchList();
		Boolean uptodate = Boolean.FALSE;
		try {
			manager.startExecution();
			for(Patch p : patchesToApply) {
				manager.apply(p);
			}
			uptodate = Boolean.TRUE;
		} finally {
			manager.endExecution();
			getPluginContext().put(getClass().getName() + "-uptodate", uptodate);			
		}
	}
    
}
