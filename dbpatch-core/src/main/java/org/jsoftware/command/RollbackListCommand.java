package org.jsoftware.command;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.EnvSettings;
import org.jsoftware.config.Patch;
import org.jsoftware.config.RollbackPatch;
import org.jsoftware.impl.CloseUtil;
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
 * Command: Runs auto-rollback mode
 *
 * @author szalik
 */
public class RollbackListCommand extends AbstractListCommand<RollbackPatch> {
    protected boolean output = true;

    public RollbackListCommand(EnvSettings envSettings) {
        super(envSettings);
    }

    @Override
    protected List<RollbackPatch> generateList(List<Patch> inListIn) throws IOException, SQLException, DuplicatePatchNameException {
        List<Patch> missingRollback = new LinkedList<Patch>();
        List<RollbackPatch> rollbacks = new LinkedList<RollbackPatch>();
        StringBuilder sb = new StringBuilder("Patch list:\n");
        List<Patch> inList = new LinkedList<Patch>(inListIn);
        Collections.reverse(inList);
        for (Patch p : inList) {
            getConfigurationEntry().getPatchParser().parse(p, getConfigurationEntry());
            sb.append('\t');
            if (p.getDbState() == AbstractPatch.DbState.COMMITTED) {
                sb.append('*');
            }
            if (p.getDbState() == AbstractPatch.DbState.IN_PROGRESS) {
                sb.append('P');
            }
            if (p.getDbState() == AbstractPatch.DbState.NOT_AVAILABLE) {
                sb.append(' ');
            }
            sb.append(' ').append(p.getName());
            for (int a = p.getName().length(); a < SPACES; a++) {
                sb.append(' ');
            }
            sb.append("  rollback: ");
            RollbackPatch rollbackPatch = findRollback(p);
            if (rollbackPatch.isMissing()) {
                missingRollback.add(p);
                sb.append("MISSING OR EMPTY");
            } else {
                sb.append("FOUND");
            }
            rollbacks.add(rollbackPatch);
            sb.append('\n');
        }
        if (output) {
            log.info(sb.toString().trim());
            if (!missingRollback.isEmpty()) {
                sb = new StringBuilder();
                for (Patch mp : missingRollback) {
                    sb.append(' ').append(mp.getName());
                }
                log.warn("Missing rollback patches: \n" + sb);
            }
        }
        return rollbacks;
    }


    private RollbackPatch findRollback(Patch patch) throws IOException, DuplicatePatchNameException {
        File rf = getConfigurationEntry().getPatchScanner().findRollbackFile(directory, configurationEntry.getRollbackDirs().split(","), patch);
        if (rf == null) {
            return new RollbackPatch(patch);
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(rf);
                PatchParser.ParseResult pr = configurationEntry.getPatchParser().parse(fis, configurationEntry);
                int sc = pr.executableCount();
                if (sc == 0) {
                    log.warn("Rollback file patch found (" + rf.getAbsolutePath() + "), but contains zero statements!");
                    return new RollbackPatch(patch);
                }
                return new RollbackPatch(patch, rf, sc);
            } finally {
                CloseUtil.close(fis);
            }
        }
    }

    @Override
    protected void executeInternal() throws Exception {
        getList();
    }
}
