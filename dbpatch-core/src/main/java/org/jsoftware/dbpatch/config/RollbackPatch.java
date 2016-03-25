package org.jsoftware.dbpatch.config;

import java.io.File;
import java.util.Date;

/**
 * Single roll-back patch
 *
 * @see Patch
 */
public class RollbackPatch extends AbstractPatch {
    private final Patch patch;
    private final File originalPatchFile;
    private boolean missing;

    public RollbackPatch(Patch patch) {
        super.setName(patch.getName());
        this.originalPatchFile = patch.getFile();
        this.missing = true;
        this.patch = patch;
    }

    public RollbackPatch(Patch patch, File rollbackFile, int rollbackStatementsCount) {
        this(patch);
        super.setFile(rollbackFile);
        super.setStatementCount(rollbackStatementsCount);
        this.missing = false;
    }


    public void setFile(File file) {
        throw new RuntimeException("DO NOT USE IT!");
    }

    @Override
    public void setDbDate(Date dbDate) {
        patch.setDbDate(dbDate);
    }

    @Override
    public void setDbState(DbState dbState) {
        patch.setDbState(dbState);
    }

    @Override
    public Date getDbDate() {
        return patch.getDbDate();
    }

    @Override
    public DbState getDbState() {
        return patch.getDbState();
    }

    public File getOriginalPatchFile() {
        return originalPatchFile;
    }

    public boolean isMissing() {
        return missing;
    }


    public boolean canApply() {
        return !isMissing() && getStatementCount() > 0 && getDbState() == DbState.COMMITTED;
    }
}

