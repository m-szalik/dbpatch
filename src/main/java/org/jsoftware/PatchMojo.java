package org.jsoftware;

import java.util.List;

import org.jsoftware.config.ApplyStrategy;
import org.jsoftware.config.Patch;
import org.jsoftware.config.Patch.DbState;
import org.jsoftware.config.PatchScaner;
import org.jsoftware.maven.AbstractSingleConfDbPatchMojo;


/**
 * Runs autopatch mode
 * @goal patch
 */
public class PatchMojo extends AbstractSingleConfDbPatchMojo {
	
	@Override
	protected void executeInternal() throws Exception {
		PatchScaner scaner = configurationEntry.getPatchScaner();
		List<Patch> patches = scaner.scan();
		manager.updateStateObjectAll(patches);
		ApplyStrategy strategy = configurationEntry.getApplayStartegy();
		log.debug("Apply strategy is " + strategy.getClass().getSimpleName());
		List<Patch> patchesToApply = strategy.filter(manager.getConnection(), patches);
		StringBuilder sb = new StringBuilder("Patch list:\n");
		for(Patch p : patches) {
			sb.append('\t');
			if (p.getDbState() == DbState.COMMITED) sb.append('*');
			if (p.getDbState() == DbState.IN_PROGRES) sb.append('P');
			if (p.getDbState() == DbState.NOT_AVAILABLE) {
				if (patchesToApply.contains(p)) sb.append('+');
				else sb.append('-');
			}
			sb.append(' ').append(p.getName()).append('\n');
		}
		log.info(sb.toString().trim());
		log.info("Patches to apply: " + patchesToApply.size());
		try {
			manager.startExecution();
			for(Patch p : patchesToApply) {
				manager.apply(p);
			}
		} finally {
			manager.endExecution();
		}
	}
    
}
