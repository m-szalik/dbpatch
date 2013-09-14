package org.jsoftware;

import org.jsoftware.impl.CloseUtil;
import org.jsoftware.maven.AbstractSingleConfDbPatchMojo;

import java.sql.*;

/**
 * Mark patches &quot;in progress&quot; as committed.
 * 
 * @goal skip-errors
 */
public class SkipErrorsMojo extends AbstractSingleConfDbPatchMojo {

	@Override
	protected void executeInternal() throws Exception {
		Connection connection = manager.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT patch_name FROM " + manager.getTableName() + " WHERE patch_db_date IS NULL");
			ps = connection.prepareStatement("UPDATE " + manager.getTableName() + " SET patch_db_date=? WHERE patch_name=?");
			ps.setDate(1, new Date(0));
			while (rs.next()) {
				ps.setString(2, rs.getString(1));
				ps.execute();
				log.info("Mark to skip " + rs.getString(1));
			}
			connection.commit();
		} finally {
			CloseUtil.close(statement);
			CloseUtil.close(rs);
			CloseUtil.close(ps);
		}
	}
}
