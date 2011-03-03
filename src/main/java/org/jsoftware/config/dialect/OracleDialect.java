package org.jsoftware.config.dialect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;

import org.jsoftware.impl.PatchStatement;

public class OracleDialect extends DefaultDialect {
	
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
	

}
