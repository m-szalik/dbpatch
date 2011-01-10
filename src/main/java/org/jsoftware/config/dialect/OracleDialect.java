package org.jsoftware.config.dialect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.jsoftware.impl.PatchStatement;

public class OracleDialect extends DefaultDialect {
	
	@Override
	public boolean executeStatement(Connection c, PatchStatement ps) throws SQLException {
		if (ps.getCode().toLowerCase().startsWith("set ")) {
			return false;
		}
		String sql = ps.getCode();
		if (sql.toLowerCase().startsWith("execute ")) {
			sql = sql.substring(8);
			sql = "{ call " + sql + " }";
			CallableStatement p = c.prepareCall(sql);
			p.execute();
			p.close();
			return true;
		}
		
		return super.executeStatement(c, ps);
	}
	

}
