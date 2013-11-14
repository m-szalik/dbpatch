package org.jsoftware.config.dialect;

import org.jsoftware.impl.PatchStatement;

import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * @author szalik
 */
public interface PatchExecutionResult {

    /**
     * @return false if org.jsoftware.config.Patch was executed with errors
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
