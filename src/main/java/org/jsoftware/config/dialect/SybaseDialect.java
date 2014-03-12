package org.jsoftware.config.dialect;

import org.jsoftware.impl.CloseUtil;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

import java.sql.*;

/**
 * Sybase dialect
 *
 * @author Chandar Rayane
 */
public class SybaseDialect extends DefaultDialect {
    private static final long serialVersionUID = -109096601395312248L;
    private static final Log logger = LogFactory.getInstance();


    @Override
    public void checkAndCreateStruct(Connection con) throws SQLException {
        boolean autoCommit = con.getAutoCommit();
        con.setAutoCommit(true);
        ResultSet rs = null;
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
            rs.next();
            return rs.getTimestamp(1);
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(stm);
        }
    }


    private void insertEmptyRow(Connection con) throws SQLException {
    }
}
