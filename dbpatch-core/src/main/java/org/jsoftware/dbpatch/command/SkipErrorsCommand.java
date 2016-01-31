package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.impl.CloseUtil;

import java.sql.*;

/**
 * Command: Mark patches &quot;in progress&quot; as committed.
 *
 * @author szalik
 */
public class SkipErrorsCommand extends AbstractSingleConfDbPatchCommand {

    public SkipErrorsCommand(EnvSettings envSettings) {
        super(envSettings);
    }


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
