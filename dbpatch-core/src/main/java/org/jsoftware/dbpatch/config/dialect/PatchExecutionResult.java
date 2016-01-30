package org.jsoftware.dbpatch.config.dialect;

import org.jsoftware.dbpatch.impl.PatchStatement;

import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * @author szalik
 */
public interface PatchExecutionResult {

    /**
     * @return false if Patch was executed with errors
     */
    boolean isFailure();

    /**
     * @return SQLWarning if any during patch execution
     */
    SQLWarning getSqlWarnings();

    /**
     * @return Fatal error during patch execution
     */
    SQLException getCause();

    PatchStatement getPatchStatement();

    int getDmlCount();

    DML_TYPE getDmlType();
}
