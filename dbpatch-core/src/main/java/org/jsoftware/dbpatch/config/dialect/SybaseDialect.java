package org.jsoftware.dbpatch.config.dialect;

import org.jsoftware.dbpatch.impl.CloseUtil;
import org.jsoftware.dbpatch.log.Log;
import org.jsoftware.dbpatch.log.LogFactory;

import java.sql.*;

/**
 * Sybase dialect
 *
 * @author Chandar Rayane
 */
public class SybaseDialect extends DefaultDialect {
    private static final long serialVersionUID = -109096601395312248L;
    private static final Log logger = LogFactory.getInstance();



    public void checkAndCreateStructure(Connection con) throws SQLException {
        boolean autoCommit = con.getAutoCommit();
        con.setAutoCommit(true);
        ResultSet rs;
        try {
            rs = con.getMetaData().getTables(null, null, DBPATCH_TABLE_NAME, null);
            boolean tableFound = rs.next();
            CloseUtil.close(rs);
            if (!tableFound) {
                Statement stm = null;
                try {
                    stm = con.createStatement();
                    stm.execute("CREATE TABLE " + DBPATCH_TABLE_NAME + "(patch_name varchar(128), patch_date datetime NULL, patch_db_date datetime NULL)");
                    insertEmptyRow(con);
                } catch (SQLException e) {
                    logger.info("An error occurred while creating '" + DBPATCH_TABLE_NAME + "' table (in Sybase database). Message: " + e.getMessage());
                    throw e;
                } finally {
                    CloseUtil.close(stm);
                }
            }
            Statement stm = null;
            try {
                stm = con.createStatement();
                rs = stm.executeQuery("SELECT patch_name FROM " + DBPATCH_TABLE_NAME + " WHERE patch_name IS NULL");
                if (!rs.next()) {
                    insertEmptyRow(con);
                }
            } finally {
                CloseUtil.close(rs);
                CloseUtil.close(stm);
            }
        } finally {
            con.setAutoCommit(autoCommit);
        }
    }

    public Timestamp getNow(Connection con) throws SQLException {
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT getDate()");
            if (rs.next()) {
                return rs.getTimestamp(1);
            } else {
                throw new SQLException("No rows returned.");
            }
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(stm);
        }
    }


    private void insertEmptyRow(Connection con) throws SQLException {
    }
}
