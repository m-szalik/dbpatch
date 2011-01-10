package org.jsoftware.config.dialect;

import java.sql.Connection;
import java.sql.SQLException;

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

	boolean executeStatement(Connection con, PatchStatement ps) throws SQLException;

	void savePatchInfoFinal(Connection con, Patch patch) throws SQLException;
	
	void savePatchInfoPrepare(Connection con, Patch patch) throws SQLException;
	
//	boolean checkStatementDelimiterChange(PatchStatement statement);
//
//	String getDelimiter();
	
}
