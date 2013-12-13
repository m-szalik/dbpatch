package org.jsoftware.command;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;
import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.RollbackPatch;

import java.util.LinkedList;
import java.util.List;


/**
 * Runs auto-patch mode
 * @author szalik
 */
public class RollbackCommand extends RollbackListCommand implements CommandSuccessIndicator {
    private boolean success;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal() throws Exception {
        success = false;
        List<RollbackPatch> patches = getList();
        List<RollbackPatch> patchesFiltered = new LinkedList<RollbackPatch>();
        String dStop = System.getProperty("maven.dbpatch.stop");
        String dSingle = System.getProperty("maven.dbpatch.single");
        if (StringUtils.isBlank(dSingle) && StringUtils.isBlank(dStop)) {
            throw new MojoExecutionException("Missing property 'maven.dbpatch.single' or 'maven.dbpatch.stop'!");
        }
        if (! StringUtils.isBlank(dSingle) && ! StringUtils.isBlank(dStop)) {
            throw new MojoExecutionException("Both properties 'maven.dbpatch.single' and 'maven.dbpatch.stop' are set!");
        }
        String pnOrg = StringUtils.isBlank(dSingle) ? dStop : dSingle;
        String pn = AbstractPatch.normalizeName(pnOrg);
        log.debug("Looking for patch '" + pnOrg + "' -> '" + pn + "'.");
        boolean found = false;
        if (StringUtils.isNotBlank(dStop)) {
            for(RollbackPatch rp : patches) {
                patchesFiltered.add(rp);
                if (rp.getName().equals(pn)) {
                    found = true;
                    break;
                }
            }
        } else {
            for(RollbackPatch rp : patches) {
                if (rp.getName().equals(pn)) {
                    found = true;
                    patchesFiltered.add(rp);
                    break;
                }
            }
        }
        if (! found) {
            throw new MojoExecutionException("Cannot find patch by name '" +pnOrg+ "'");
        }

		try {
			manager.startExecution();
			for(RollbackPatch p : patchesFiltered) {
                if (p.getDbState() == AbstractPatch.DbState.COMMITTED) {
                    if (p.isMissing()) {
                        String msg = "Missing or empty rollback file for patch " + p.getName();
                        log.fatal(msg);
                        throw new MojoExecutionException(msg);
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

    @Override
    public boolean isSuccess() {
        return success;
    }
}
