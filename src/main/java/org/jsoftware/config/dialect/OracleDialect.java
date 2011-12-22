package org.jsoftware.config.dialect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;

import org.jsoftware.impl.PatchStatement;

public class OracleDialect extends DefaultDialect {
	private static final long serialVersionUID = -8744726190040823430L;

	@Override
	public PatchExecutionResult executeStatement(Connection c, PatchStatement ps) {
		if (ps.getCode().toLowerCase().startsWith("set ")) {
			PatchExecutionResultImpl resultImpl = new PatchExecutionResultImpl(ps);
			resultImpl.setSqlWarning(new SQLWarning("dbPatch do not allow executinig SET statements"));
			return resultImpl;
		}
		String sql = ps.getCode();
		if (sql.toLowerCase().startsWith("execute ")) {
			sql = sql.substring(8);
			sql = "{ call " + sql + " }";
			PatchExecutionResultImpl result = new PatchExecutionResultImpl(ps);
			try {
				c.clearWarnings();
				CallableStatement p = c.prepareCall(sql);
				p.execute();
				p.close();
				result.setSqlWarning(c.getWarnings());
			} catch (SQLException e) {
				result.setCause(e);
			}
			return result;
		}
		
		return super.executeStatement(c, ps);
	}
	
	
	public Timestamp getNow(Connection con) throws SQLException {
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery("SELECT sysdate FROM dual");
		Timestamp ts = null;
		try {
			rs.next();
			ts = rs.getTimestamp(1);
			rs.close();
		} finally {
			stm.close();
			rs.close();
		}
		return ts;
	}

}
