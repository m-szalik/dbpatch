package org.jsoftware.dbpatch.config.dialect;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.Patch;
import org.jsoftware.dbpatch.config.RollbackPatch;
import org.jsoftware.dbpatch.impl.CloseUtil;
import org.jsoftware.dbpatch.impl.PatchStatement;
import org.jsoftware.dbpatch.log.LogFactory;;

import java.sql.*;

/**
 * Default dialect
 * Works with: MySQL, PostgreSQL, H2
 *
 * @author szalik
 */
public class DefaultDialect implements Dialect {
    private static final long serialVersionUID = 1L;


    public String getDbPatchTableName() {
        return DBPATCH_TABLE_NAME;
    }

    public PatchExecutionResult executeStatement(Connection c, PatchStatement ps) {
        PatchExecutionResultImpl result = new PatchExecutionResultImpl(ps);
        Statement stm = null;
        try {
            c.clearWarnings();
            stm = c.createStatement();
            executeSql(stm, ps.getCode(), result);
        } catch (SQLException e) {
            result.setCause(e);
            try {
                result.setSqlWarning(c.getWarnings());
            } catch (SQLException ex) {
                LogFactory.getInstance().warn("Cannot set warnings.", ex);
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex2) { /* ignore */ }
            }
        }
        return result;
    }

    private void executeSql(Statement stm, String sql, PatchExecutionResultImpl result) throws SQLException {
        String sqlLC = sql.toLowerCase().trim();
        if (sqlLC.startsWith("insert ") || sqlLC.startsWith("update ") || sqlLC.startsWith("delete ")) {
            int duiCount = stm.executeUpdate(sql);
            result.setDmlCount(duiCount);
            String type = sqlLC.substring(0, sqlLC.indexOf(' ')).toUpperCase();
            result.setDmlType(DML_TYPE.valueOf(type));
        } else {
            stm.execute(sql);
        }
    }

    public void lock(Connection con, long timeout) throws SQLException {
//		ResultSet rs = con.createStatement().executeQuery("SELECT patch_db_date FROM " + DBPATCH_TABLE_NAME + " WHERE patch_name IS NULL");
//		try {
//			if (! rs.next()) {
//				throw new RuntimeException("No empty row in " + DBPATCH_TABLE_NAME + " table.");
//			}
//			if (rs.getTimestamp(1) == null) {
//				PreparedStatement ps = con.prepareStatement("UPDATE " + DBPATCH_TABLE_NAME + " SET patch_db_date = ? WHERE patch_name IS NULL");
//				ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
//				ps.execute();
//				ps.close();
//				con.commit();
//			} else {
//				throw new SQLException("Database is locked.");
//			}
//		} finally {
//			rs.close();
//		}
    }

    public void releaseLock(Connection con) throws SQLException {
//		Statement stm = con.createStatement();
//		stm.executeUpdate("UPDATE " + DBPATCH_TABLE_NAME + " SET patch_db_date = NULL WHERE patch_name IS NULL");
//		stm.close();
//		con.commit();
    }

    public void savePatchInfoPrepare(Connection con, Patch patch) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT * FROM " + DBPATCH_TABLE_NAME + " WHERE patch_name=? AND patch_db_date IS NULL");
            ps.setString(1, patch.getName());
            rs = ps.executeQuery();
            boolean addRow = !rs.next();
            rs.close();
            ps.close();
            if (addRow) {
                ps = con.prepareStatement("INSERT INTO " + DBPATCH_TABLE_NAME + " (patch_name,patch_date,patch_db_date) VALUES (?,?,NULL)");
                ps.setString(1, patch.getName());
                ps.setTimestamp(2, new java.sql.Timestamp(patch.getFile().lastModified()));
                ps.execute();
            }
            con.commit();
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(ps);
        }
    }

    public void savePatchInfoFinal(Connection con, Patch patch) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE " + DBPATCH_TABLE_NAME + " SET patch_db_date=? WHERE patch_name=?");
            ps.setTimestamp(1, getNow(con));
            ps.setString(2, patch.getName());
            ps.execute();
        } finally {
            CloseUtil.close(ps);
        }
    }

    @Override
    public void removePatchInfo(Connection con, RollbackPatch p) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("DELETE FROM " + DBPATCH_TABLE_NAME + " WHERE patch_name=?");
            ps.setString(1, p.getName());
            ps.execute();
        } finally {
            CloseUtil.close(ps);
        }
    }

    @Override
    public boolean checkIfPatchIsCommitted(Connection con, AbstractPatch patch) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT * FROM " + DBPATCH_TABLE_NAME + " WHERE patch_name=?");
            ps.setString(1, patch.getName());
            rs = ps.executeQuery();
            return rs.next();
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(ps);
        }
    }

    public void checkAndCreateStructure(Connection con) throws SQLException {
        con.setAutoCommit(true);
        ResultSet rs = con.getMetaData().getTables(null, null, DBPATCH_TABLE_NAME, null);
        boolean tableFound = rs.next();
        rs.close();
        Statement stm = null;
        if (!tableFound) {
            try {
                stm = con.createStatement();
                stm.execute("CREATE TABLE " + DBPATCH_TABLE_NAME + "(patch_name varchar(128), patch_date TIMESTAMP, patch_db_date TIMESTAMP)");
                insertEmptyRow(con);
            } catch (SQLException e) { /* ignore */ } finally {
                CloseUtil.close(stm);
            }
        }
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
        con.setAutoCommit(false);
    }

    private void insertEmptyRow(Connection con) throws SQLException {
//		Statement stm = con.createStatement();
//		stm.execute("INSERT INTO " + DBPATCH_TABLE_NAME + "(patch_name, patch_date, patch_db_date) VALUES (NULL, NULL, NULL)");
//		stm.close();
//		con.commit();
    }

    public Timestamp getNow(Connection con) throws SQLException {
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT now()");
            rs.next();
            return rs.getTimestamp(1);
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(stm);
        }
    }


}
