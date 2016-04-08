package org.jsoftware.dbpatch.impl.extension;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.ApplyStrategy;
import org.jsoftware.dbpatch.config.Patch;
import org.jsoftware.dbpatch.config.RollbackPatch;
import org.jsoftware.dbpatch.config.dialect.PatchExecutionResult;
import org.jsoftware.dbpatch.impl.CloseUtil;
import org.jsoftware.dbpatch.impl.PatchStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TkExtensionAndStrategy implements Extension, ApplyStrategy {

    private static int patchLevel(Patch p) {
        String str = p.getFile().getName();
        try {
            str = str.substring(5, 5 + 4).trim();
            return Integer.parseInt(str);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not determinate patchLeave for patch " + p.getName());
        }
    }

    public List<Patch> filter(Connection con, List<Patch> patches) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            String tabName = detectTkTableName(con);
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT patch_level FROM " + tabName);
            if (! rs.next()) {
                throw new IllegalStateException("No entries in 'patch_level' table.");
            }
            int currentPatchLevel = rs.getInt(1);
            rs.close();
            LinkedList<Patch> patchesToApply = new LinkedList<Patch>();
            for (Patch p : patches) {
                if (patchLevel(p) > currentPatchLevel) {
                    patchesToApply.add(p);
                }
            }
            return patchesToApply;
        } catch (Exception e) {
            throw new IllegalStateException("Can not apply strategy.", e);
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(stm);
        }
    }

    private String detectTkTableName(Connection con) {
        String tabName = null;
        Set<String> qm = new HashSet<String>();
        try {
            ResultSet rs = con.getMetaData().getTables(null, null, null, null);
            while (rs.next()) {
                String tb = rs.getString(3);
                if ("patches".equalsIgnoreCase(tb) || "tk_patches".equalsIgnoreCase(tb)) {
                    qm.add(tb);
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (qm.size() == 1) {
            tabName = qm.iterator().next();
        }
        if (tabName == null) {
            throw new RuntimeException("No tk table available. " + qm);
        }
        return tabName;
    }

    public void beforePatching(Connection connection) {
        // nothing to do here
    }

    public void afterPatching(Connection connection) {
        // nothing to do here
    }

    public void beforePatch(Connection connection, Patch patch) {
        // nothing to do here
    }

    public void afterPatch(Connection connection, Patch patch, Exception ex) throws SQLException {
        if (ex == null) {
            Statement stm = null;
            try {
                stm = connection.createStatement();
                stm.executeUpdate("UPDATE " + detectTkTableName(connection) + " SET patch_level=" + patchLevel(patch));
            } finally {
                CloseUtil.close(stm);
            }
        }
    }


    public void beforePatchStatement(Connection connection, AbstractPatch patch, PatchStatement statement) {

    }


    public void afterPatchStatement(Connection connection, AbstractPatch patch, PatchExecutionResult result) {

    }


    public void beforeRollbackPatch(Connection connection, RollbackPatch patch) {

    }


    public void afterRollbackPatch(Connection connection, RollbackPatch patch, Exception ex) throws SQLException {

    }


}
