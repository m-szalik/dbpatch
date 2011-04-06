package org.jsoftware;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jsoftware.maven.AbstractSingleConfDbPatchMojo;

/**
 * Mark patches &quot;in progress&quot; as commited.
 * 
 * @goal skipErrors
 */
public class SkipErrorsMojo extends AbstractSingleConfDbPatchMojo {

	@Override
	protected void executeInternal() throws Exception {
		Connection connection = manager.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT patch_name FROM " + manager.getTableName() + " WHERE patch_db_date IS NULL");
		PreparedStatement ps = connection.prepareStatement("UPDATE " + manager.getTableName() + " SET patch_db_date=? WHERE patch_name=?");
		ps.setDate(1, new Date(0));
		while (rs.next()) {
			ps.setString(2, rs.getString(1));
			ps.execute();
			log.info("Mark to skip " + rs.getString(1));
		}
		connection.commit();
	}
}
