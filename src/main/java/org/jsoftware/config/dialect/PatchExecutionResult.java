package org.jsoftware.config.dialect;

import org.jsoftware.impl.PatchStatement;

import java.sql.SQLException;
import java.sql.SQLWarning;


public interface PatchExecutionResult {
	
	boolean isFailure();
	
	SQLWarning getSqlWarnings();
	
	SQLException getCause();
	
	PatchStatement getPatchStatement();
	
	int getDmlCount();
	
	DML_TYPE getDmlType();
}
