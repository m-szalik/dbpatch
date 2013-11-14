package org.jsoftware.command;

import org.jsoftware.config.ApplyStrategy;
import org.jsoftware.config.Patch;
import org.jsoftware.config.Patch.DbState;
import org.jsoftware.config.PatchScanner;
import org.jsoftware.impl.DuplicatePatchNameException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * Show list of patches
 * @author szalik
 */
public class ListCommand extends AbstractSingleConfDbPatchCommand {
	
	/**
	 * @return patches to apply
	 * @throws java.io.IOException
	 * @throws java.sql.SQLException
	 * @throws org.jsoftware.impl.DuplicatePatchNameException
	 */
	List<Patch> generatePatchList() throws IOException, SQLException, DuplicatePatchNameException {
		PatchScanner scanner = configurationEntry.getPatchScanner();
		List<Patch> patches = scanner.scan(directory, configurationEntry.getPatchDirs().split(","));
		manager.updateStateObjectAll(patches);
		ApplyStrategy strategy = configurationEntry.getApplyStarters();
		log.debug("Apply strategy is " + strategy.getClass().getSimpleName() + ", configurationId:" + configurationEntry.getId());
		List<Patch> patchesToApply = strategy.filter(manager.getConnection(), patches);
		StringBuilder sb = new StringBuilder("Patch list:\n");
		for(Patch p : patches) {
			configurationEntry.getPatchParser().parse(p, configurationEntry);
			sb.append('\t');
			if (p.getDbState() == DbState.COMMITTED) sb.append('*');
			if (p.getDbState() == DbState.IN_PROGRESS) sb.append('P');
			if (p.getDbState() == DbState.NOT_AVAILABLE) {
				if (patchesToApply.contains(p)) sb.append('+');
				else sb.append('-');
			}
			sb.append(' ').append(p.getName());
			for(int a=p.getName().length(); a<22; a++) {
				sb.append(' ');
			}
			sb.append("  statements:").append(p.getStatementCount());
			sb.append('\n');
		}
        log.info(sb.toString().trim());
		log.info("Patches to apply: " + patchesToApply.size() + " using configuration:" + configurationEntry.getId());
		return patchesToApply;
	}
	
	@Override
	protected void executeInternal() throws Exception {
		generatePatchList();
	}
    
}
