package org.jsoftware.command;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.Patch;
import org.jsoftware.config.RollbackPatch;
import org.jsoftware.impl.DuplicatePatchNameException;
import org.jsoftware.impl.PatchParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Runs auto-patch mode
 * @author szalik
 */
public class RollbackListCommand extends AbstractListCommand<RollbackPatch> {
    protected boolean output = true;

    @Override
    protected List<RollbackPatch> generateList(List<Patch> inList) throws IOException, SQLException, DuplicatePatchNameException {
        List<Patch> missingRollback = new LinkedList<Patch>();
        List<RollbackPatch> rollbacks = new LinkedList<RollbackPatch>();
        StringBuilder sb = new StringBuilder("Patch list:\n");
        for(Patch p : inList) {
            getConfigurationEntry().getPatchParser().parse(p, getConfigurationEntry());
            sb.append('\t');
            if (p.getDbState() == AbstractPatch.DbState.COMMITTED) sb.append('*');
            if (p.getDbState() == AbstractPatch.DbState.IN_PROGRESS) sb.append('P');
            if (p.getDbState() == AbstractPatch.DbState.NOT_AVAILABLE) sb.append(' ');
            sb.append(' ').append(p.getName());
            for(int a=p.getName().length(); a<22; a++) {
                sb.append(' ');
            }
            sb.append("  rollback: ");
            RollbackPatch rollbackPatch = findRollback(p);
            if (rollbackPatch.isMissing()) {
                missingRollback.add(p);
                sb.append("MISSING OR EMPTY");
            } else {
                sb.append("OK");
            }
            rollbacks.add(rollbackPatch);
            sb.append('\n');
        }
        if (output) {
            log.info(sb.toString().trim());
            if (! missingRollback.isEmpty()) {
                sb = new StringBuilder();
                for(Patch mp : missingRollback) {
                    sb.append('\t').append(mp.getName()).append('\n');
                }
                log.warn("Missing rollback patches: \n" + sb);
            }
        }
        Collections.reverse(rollbacks);
        return rollbacks;
    }

    private RollbackPatch findRollback(Patch patch) throws IOException, DuplicatePatchNameException {
        File rf = getConfigurationEntry().getPatchScanner().findRollbackFile(directory, configurationEntry.getPatchDirs().split(",") ,patch);
        if (rf == null) {
            return new RollbackPatch(patch);
        } else {
            PatchParser.ParseResult pr = configurationEntry.getPatchParser().parse(new FileInputStream(rf), configurationEntry);
            int sc = pr.executableCount();
            if (sc == 0) {
                log.warn("Rollback file patch found (" + rf.getAbsolutePath() + "), but contains zero statements!");
                return new RollbackPatch(patch);
            }
            return new RollbackPatch(patch, rf, sc);
        }
    }

    @Override
    protected void executeInternal() throws Exception {
        getList();
    }
}
