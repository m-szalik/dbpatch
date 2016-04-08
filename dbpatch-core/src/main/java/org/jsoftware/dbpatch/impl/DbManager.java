package org.jsoftware.dbpatch.impl;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.config.Patch;
import org.jsoftware.dbpatch.config.RollbackPatch;
import org.jsoftware.dbpatch.config.dialect.Dialect;
import org.jsoftware.dbpatch.config.dialect.PatchExecutionResult;
import org.jsoftware.dbpatch.impl.extension.Extension;
import org.jsoftware.dbpatch.impl.statements.DisallowedSqlPatchStatement;
import org.jsoftware.dbpatch.log.Log;
import org.jsoftware.dbpatch.log.LogFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DbManager {
    private final ConfigurationEntry ce;
    private final Dialect dialect;
    private final Log log;
    private final List<Extension> extensions;
    private Connection c;


    public DbManager(ConfigurationEntry ce) throws SQLException {
        this.log = LogFactory.getInstance();
        this.ce = ce;
        this.extensions = new LinkedList<Extension>(ce.getExtensions());
        this.dialect = ce.getDialect();
        try {
            Class.forName(ce.getDriverClass()).newInstance();
        } catch (Exception ex) {
            throw new SQLException("Could not load driver class - " + ce.getDriverClass());
        }
    }

    public void init(DbManagerCredentialsCallback dbManagerPasswordCallback) throws SQLException {
        Connection con = null;
        int tryNo = 0;
        String password = ce.getPassword();
        boolean connected = false;
        do {
            try {
                con = DriverManager.getConnection(ce.getJdbcUri(), ce.getUser(), password);
                connected = true;
            } catch (SQLException e) {
                password = dbManagerPasswordCallback.getPassword(e, tryNo, ce);
                tryNo++;
            }
        } while (! connected);
        if (con != null) {
            dialect.checkAndCreateStructure(con);
            con.setAutoCommit(false); // just for sure
            c = con;
        }
    }

    public Connection getConnection() {
        if (c == null) {
            throw new IllegalStateException("Invoke init method first.");
        }
        return c;
    }

    public void updateStateObject(AbstractPatch p) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT patch_db_date FROM " + dialect.getDbPatchTableName() + " WHERE patch_name=?");
            ps.setString(1, p.getName());
            rs = ps.executeQuery();
            if (rs.next()) {
                Date d = rs.getDate(1);
                if (d != null) {
                    p.setDbState(AbstractPatch.DbState.COMMITTED);
                    p.setDbDate(d);
                } else {
                    p.setDbState(AbstractPatch.DbState.IN_PROGRESS);
                }
            } else {
                p.setDbDate(null);
                p.setDbState(AbstractPatch.DbState.NOT_AVAILABLE);
            }
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(ps);
        }
    }

    public void apply(final Patch p) throws SQLException {
        PatchStatementHolder h = new PatchStatementHolder();
        try {
            log.info("Patch " + p.getName());
            dialect.savePatchInfoPrepare(c, p);
            invokeExtensions("beforePatch", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.beforePatch(c, p);
                }
            });
            execute(p, h);
            dialect.savePatchInfoFinal(c, p);
            invokeExtensions("afterPatchComplete", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.afterPatch(c, p, null);
                }
            });
            c.commit();
            log.debug("Patch " + p.getName() + " committed.");
        } catch (Exception e) {
            if (h.object != null) {
                log.warn("Query execution problem \"" + h.object + "\" - " + e);
            }
            log.warn("Patch " + p.getName() + " execution error!" + e);
            c.rollback();
            final SQLException ex = new SQLException(e.getMessage(), "");
            ex.initCause(e);
            invokeExtensions("afterPatchError", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.afterPatch(c, p, ex);
                }
            });
            throw ex;
        } finally {
            updateStateObject(p);
        }
    }

    /**
     * @param p rollback patch
     * @throws SQLException sql problem
     */
    public void rollback(final RollbackPatch p) throws SQLException {
        PatchStatementHolder h = new PatchStatementHolder();
        try {
            log.info("Patch " + p.getName());
            invokeExtensions("beforeRollbackPatch", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.beforeRollbackPatch(c, p);
                }
            });
            execute(p, h);
            invokeExtensions("afterRollbackPatchComplete", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.afterRollbackPatch(c, p, null);
                }
            });
            dialect.removePatchInfo(c, p);
            c.commit();
            log.debug("Patch " + p.getName() + " committed.");
        } catch (Exception e) {
            if (h.object != null) {
                log.warn("Query execution problem \"" + h.object + "\" - " + e);
            }
            log.warn("Patch " + p.getName() + " execution error!" + e);
            c.rollback();
            final SQLException ex = new SQLException(e.getMessage(), "");
            ex.initCause(e);
            invokeExtensions("afterRollbackPatchError", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.afterRollbackPatch(c, p, ex);
                }
            });
            throw ex;
        } finally {
            updateStateObject(p);
        }
    }


    private void execute(final AbstractPatch patch, final PatchStatementHolder h) throws IOException, SQLException {
        for (final PatchStatement ps : ce.getPatchParser().parse(patch, ce).getStatements()) {
            if (ps.isDisplayable()) {
                log.debug(ps.toString());
            }
            if (ps.isExecutable()) {
                h.object = ps;
                invokeExtensions("beforePatchStatement", new ExtensionMethodInvokeCallback() {
                    public void invokeOn(Extension extension) throws Exception {
                        extension.beforePatchStatement(c, patch, ps);
                    }
                });
                final PatchExecutionResult result = dialect.executeStatement(c, ps);
                invokeExtensions("afterPatchStatement", new ExtensionMethodInvokeCallback() {
                    public void invokeOn(Extension extension) throws Exception {
                        extension.afterPatchStatement(c, patch, result);
                    }
                });
                if (result.getCause() != null) {
                    throw result.getCause();
                }
            }
            if (ps instanceof DisallowedSqlPatchStatement) {
                log.warn("Skip disallowed statement " + ps.getCode());
            }
        } // for
        h.object = null;
    }

    public void updateStateObjectAll(Collection<Patch> patches) throws SQLException {
        for (Patch p : patches) {
            updateStateObject(p);
        }
    }


    public void startExecution() throws SQLException {
        dialect.lock(c, 3000);
        invokeExtensions("beforePatching", new ExtensionMethodInvokeCallback() {
            public void invokeOn(Extension extension) throws Exception {
                extension.beforePatching(c);
            }
        });
    }

    public void endExecution() throws SQLException {
        try {
            c.rollback();
        } finally {
            invokeExtensions("afterPatching", new ExtensionMethodInvokeCallback() {
                public void invokeOn(Extension extension) throws Exception {
                    extension.afterPatching(c);
                }
            });
            dialect.releaseLock(c);
        }
    }


    public void dispose() {
        CloseUtil.close(c);
    }

    private void invokeExtensions(String method, ExtensionMethodInvokeCallback cb) {
        for (Extension extension : extensions) {
            log.debug("Invoke method (" + method + ") on " + extension.getClass());
            try {
                cb.invokeOn(extension);
            } catch (Exception e) {
                log.warn("Invoke method (" + method + ") on " + extension.getClass() + " throws " + e, e);
            }
        }
    }

    public void addExtension(Extension extension) {
        extensions.add(extension);
    }

    public String getTableName() {
        return dialect.getDbPatchTableName();
    }

    public Date getNow() throws SQLException {
        Timestamp ts = dialect.getNow(c);
        return new Date(ts.getTime());
    }

}

interface ExtensionMethodInvokeCallback {
    void invokeOn(Extension extension) throws Exception;
}

class PatchStatementHolder {
    public PatchStatement object;
}