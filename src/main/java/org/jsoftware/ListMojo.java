package org.jsoftware;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.jsoftware.config.ApplyStrategy;
import org.jsoftware.config.Patch;
import org.jsoftware.config.PatchScaner;
import org.jsoftware.config.Patch.DbState;
import org.jsoftware.maven.AbstractSingleConfDbPatchMojo;


/**
 * Show list of patches
 * @goal list
 */
public class ListMojo extends AbstractSingleConfDbPatchMojo {
	
	/**
	 * 
	 * @return patches to apply
	 * @throws IOException 
	 * @throws SQLException 
	 */
	List<Patch> generatePatchList() throws IOException, SQLException {
		PatchScaner scaner = configurationEntry.getPatchScaner();
		List<Patch> patches = scaner.scan(directory, configurationEntry.getPatchDirs().split(","));
		manager.updateStateObjectAll(patches);
		ApplyStrategy strategy = configurationEntry.getApplayStartegy();
		log.debug("Apply strategy is " + strategy.getClass().getSimpleName() + ", configurationId:" + configurationEntry.getId());
		List<Patch> patchesToApply = strategy.filter(manager.getConnection(), patches);
		StringBuilder sb = new StringBuilder("Patch list:\n");
		for(Patch p : patches) {
			configurationEntry.getPatchParser().parse(p);
			sb.append('\t');
			if (p.getDbState() == DbState.COMMITED) sb.append('*');
			if (p.getDbState() == DbState.IN_PROGRES) sb.append('P');
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
		};
		log.info(sb.toString().trim());
		log.info("Patches to apply: " + patchesToApply.size() + " using configuration:" + configurationEntry.getId());
		return patchesToApply;
	}
	
	@Override
	protected void executeInternal() throws Exception {
		generatePatchList();
	}
    
}
