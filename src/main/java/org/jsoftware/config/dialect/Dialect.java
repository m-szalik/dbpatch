package org.jsoftware.config.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jsoftware.config.Patch;
import org.jsoftware.impl.PatchStatement;

public interface Dialect {
	
	public interface DelimiterChangeEvent {
		boolean isChanged();
		String getDelimiter();
	}
	
	String getDbPatchTableName();
	
	void lock(Connection con, long timeout) throws SQLException;
	
	void releaseLock(Connection con) throws SQLException;
		
	void checkAndCreateStruct(Connection con) throws SQLException;

	PatchExecutionResult executeStatement(Connection con, PatchStatement ps);

	void savePatchInfoFinal(Connection con, Patch patch) throws SQLException;
	
	void savePatchInfoPrepare(Connection con, Patch patch) throws SQLException;
	
//	boolean checkStatementDelimiterChange(PatchStatement statement);
//
//	String getDelimiter();

	Timestamp getNow(Connection con) throws SQLException;
}
