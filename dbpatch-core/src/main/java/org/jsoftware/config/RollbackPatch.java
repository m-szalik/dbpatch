package org.jsoftware.config;

import java.io.File;

/**
 * Single roll-back patch
 *
 * @see org.jsoftware.config.Patch
 */
public class RollbackPatch extends AbstractPatch {

    private final File originalPatchFile;
    private boolean missing;

    public RollbackPatch(Patch patch) {
        super.setName(patch.getName());
        super.setDbDate(patch.getDbDate());
        super.setDbState(patch.getDbState());
        this.originalPatchFile = patch.getFile();
        this.missing = true;
    }

    public RollbackPatch(Patch patch, File rollbackFile, int rollbackStatementsCount) {
        this(patch);
        super.setFile(rollbackFile);
        super.setStatementCount(rollbackStatementsCount);
        this.missing = false;
    }

    @Override
    public void setFile(File file) {
        throw new RuntimeException("DO NOT USE IT!");
    }

    public File getOriginalPatchFile() {
        return originalPatchFile;
    }

    public boolean isMissing() {
        return missing;
    }

    @Override
    public boolean canApply() {
        return !isMissing() && getStatementCount() > 0 && getDbState() == DbState.COMMITTED;
    }
}

