package org.jsoftware.command;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.ApplyStrategy;
import org.jsoftware.config.Patch;

import java.io.IOException;
import java.util.List;


/**
 * Show list of patches
 * @author szalik
 */
public class ListCommand extends AbstractListCommand<Patch> {

    @Override
    protected List<Patch> generateList(List<Patch> inList) throws IOException {
        ApplyStrategy strategy = configurationEntry.getApplyStarters();
        log.debug("Apply strategy is " + strategy.getClass().getSimpleName() + ", configurationId:" + configurationEntry.getId());
        List<Patch> patchesToApply = strategy.filter(manager.getConnection(), inList);
        StringBuilder sb = new StringBuilder("Patch list:\n");
        for(Patch p : inList) {
            getConfigurationEntry().getPatchParser().parse(p, getConfigurationEntry());
            sb.append('\t');
            if (p.getDbState() == AbstractPatch.DbState.COMMITTED) sb.append('*');
            if (p.getDbState() == AbstractPatch.DbState.IN_PROGRESS) sb.append('P');
            if (p.getDbState() == AbstractPatch.DbState.NOT_AVAILABLE) {
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
		getList();
	}
    
}
