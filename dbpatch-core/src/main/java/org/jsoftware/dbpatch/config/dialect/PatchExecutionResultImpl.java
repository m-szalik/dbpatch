package org.jsoftware.dbpatch.config.dialect;

import org.jsoftware.dbpatch.impl.PatchStatement;

import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * @see PatchExecutionResult
 */
public class PatchExecutionResultImpl implements PatchExecutionResult {
    private SQLWarning sqlWarning;
    private SQLException cause;
    private int dmlCount = -1;
    private DML_TYPE dmlType;
    private final PatchStatement patchStatement;


    public PatchExecutionResultImpl(PatchStatement patchStatement) {
        this.patchStatement = patchStatement;
    }

    public void setCause(SQLException cause) {
        this.cause = cause;
    }

    public void setSqlWarning(SQLWarning sqlWarning) {
        this.sqlWarning = sqlWarning;
    }

    public SQLWarning getSqlWarnings() {
        return sqlWarning;
    }

    public boolean isFailure() {
        return cause != null;
    }

    public SQLException getCause() {
        return cause;
    }


    public PatchStatement getPatchStatement() {
        return patchStatement;
    }

    public int getDmlCount() {
        return dmlCount;
    }

    public void setDmlCount(int dmlCount) {
        this.dmlCount = dmlCount;
    }

    public DML_TYPE getDmlType() {
        return dmlType;
    }

    public void setDmlType(DML_TYPE dmlType) {
        this.dmlType = dmlType;
    }
}
