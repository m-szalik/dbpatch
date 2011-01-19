package org.jsoftware;

import java.util.List;

import org.jsoftware.config.Patch;


/**
 * Runs autopatch mode
 * @goal patch
 */
public class PatchMojo extends ListMojo {
	
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
