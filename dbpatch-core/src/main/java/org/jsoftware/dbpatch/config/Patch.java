package org.jsoftware.dbpatch.config;

/**
 * Single patch
 *
 * @see RollbackPatch
 */
public class Patch extends AbstractPatch {


    public boolean canApply() {
        return getStatementCount() > 0 && getDbState() == DbState.NOT_AVAILABLE;
    }
}
