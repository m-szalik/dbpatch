package org.jsoftware.config;

/**
 * Single patch
 *
 * @see org.jsoftware.config.RollbackPatch
 */
public class Patch extends AbstractPatch {

    @Override
    public boolean canApply() {
        return getStatementCount() > 0 && getDbState() == DbState.NOT_AVAILABLE;
    }
}
