package org.jsoftware.dbpatch.config.dialect;

import org.jsoftware.dbpatch.impl.CloseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * HsqlDb dialect
 *
 * @author szalik
 */
public class HsqlDialect extends DefaultDialect {
    private static final long serialVersionUID = -1090776645312248L;

    public Timestamp getNow(Connection con) throws SQLException {
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("VALUES (CURRENT_TIMESTAMP)");
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

}
