package org.jsoftware.config.dialect;

import java.sql.SQLException;
import java.sql.SQLWarning;

import org.jsoftware.impl.PatchStatement;


public interface PatchExecutionResult {
	
	public enum DML_TYPE { UPDATE, INSERT, DELETE }
	
	boolean isSuccess();
	
	SQLWarning getSqlWarnings();
	
	SQLException getCause();
	
	PatchStatement getPatchStatement();
	
	int getDmlCount();
	
	DML_TYPE getDmlType();
}
