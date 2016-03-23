package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.config.RollbackPatch;
import org.jsoftware.dbpatch.impl.StringUtils;

import java.util.LinkedList;
import java.util.List;


/**
 * Command: Runs auto-patch mode
 *
 * @author szalik
 */
public class RollbackCommand extends RollbackListCommand implements CommandSuccessIndicator {
    private boolean success;
    private RollbackCommandConf rollbackCommandConf;

    public RollbackCommand(EnvSettings envSettings) {
        super(envSettings);
    }

    void setRollbackCommandConf(RollbackCommandConf rollbackCommandConf) {
        this.rollbackCommandConf = rollbackCommandConf;
    }

    @SuppressWarnings("unchecked")
    protected void executeInternal() throws Exception {
        success = false;
        List<RollbackPatch> patches = getList();
        List<RollbackPatch> patchesFiltered = new LinkedList<RollbackPatch>();
        if (rollbackCommandConf == null) {
            rollbackCommandConf = createRollbackCommandConf();
        }
        log.debug("Looking for patch '" + rollbackCommandConf.getPatchName() + "'.");
        boolean found = false;
        if (rollbackCommandConf.getAction() == RollbackCommandConf.Action.STOP_ON) {
            for (RollbackPatch rp : patches) {
                patchesFiltered.add(rp);
                if (rp.getName().equals(rollbackCommandConf.getPatchName())) {
                    found = true;
                    break;
                }
            }
        } else {
            for (RollbackPatch rp : patches) {
                if (rp.getName().equals(rollbackCommandConf.getPatchName())) {
                    found = true;
                    patchesFiltered.add(rp);
                    break;
                }
            }
        }
        if (!found) {
            throw new CommandFailureException("Cannot find patch by name '" + rollbackCommandConf.getPatchName() + "'");
        }

        try {
            manager.startExecution();
            for (RollbackPatch p : patchesFiltered) {
                if (p.getDbState() == AbstractPatch.DbState.COMMITTED) {
                    if (p.isMissing()) {
                        String msg = "Missing or empty rollback file for patch " + p.getName();
                        log.fatal(msg);
                        throw new CommandFailureException(msg);
                    } else {
                        log.info("Executing rollback for " + p.getName() + " - " + p.getFile().getName());
                        manager.rollback(p);
                    }
                } else {
                    log.info("Skipping rollback for " + p.getName() + " patch was not applied.");
                }
            }
            success = true;
        } finally {
            manager.endExecution();
        }
    }

    private RollbackCommandConf createRollbackCommandConf() throws CommandFailureException {
        String dStop = System.getProperty(envSettings.getDbPatchStop());
        String dSingle = System.getProperty(envSettings.getDbPatchSingle());
        if (StringUtils.isBlank(dSingle) && StringUtils.isBlank(dStop)) {
            throw new CommandFailureException("Missing property '" + envSettings.getDbPatchSingle() + "' or '" + envSettings.getDbPatchStop() + "'!");
        }
        if (!StringUtils.isBlank(dSingle) && !StringUtils.isBlank(dStop)) {
            throw new CommandFailureException("Both properties '" + envSettings.getDbPatchSingle() + "' and '" + envSettings.getDbPatchStop() + "' are set!");
        }
        RollbackCommandConf.Action action = StringUtils.isBlank(dSingle) ? RollbackCommandConf.Action.STOP_ON : RollbackCommandConf.Action.SINGLE;
        String pnOrg = action == RollbackCommandConf.Action.STOP_ON ? dStop : dSingle;
        String patchName = AbstractPatch.normalizeName(pnOrg);
        return new RollbackCommandConf(action, patchName);
    }


    public boolean isSuccess() {
        return success;
    }
}
