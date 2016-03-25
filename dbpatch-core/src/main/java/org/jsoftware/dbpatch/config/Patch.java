package org.jsoftware.dbpatch.config;

import java.util.Date;

/**
 * Single patch
 *
 * @see RollbackPatch
 */
public class Patch extends AbstractPatch {
    private DbState dbState;
    private Date dbDate;

    @Override
    public void setDbDate(Date dbDate) {
        this.dbDate = dbDate == null ? null : new Date(dbDate.getTime());
    }

    @Override
    public void setDbState(DbState dbState) {
        this.dbState = dbState;
    }

    @Override
    public Date getDbDate() {
        return dbDate == null ? null : new Date(dbDate.getTime());
    }

    @Override
    public DbState getDbState() {
        return dbState;
    }

    public boolean canApply() {
        return getStatementCount() > 0 && getDbState() == DbState.NOT_AVAILABLE;
    }
}
