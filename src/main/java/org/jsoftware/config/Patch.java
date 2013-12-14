package org.jsoftware.config;

public class Patch extends AbstractPatch {
    @Override
    public boolean canApply() {
        return getStatementCount() > 0 && getDbState() == DbState.NOT_AVAILABLE;
    }
}
