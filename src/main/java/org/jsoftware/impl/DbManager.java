package org.jsoftware.impl;

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

import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.config.Patch;
import org.jsoftware.config.Patch.DbState;
import org.jsoftware.config.dialect.Dialect;
import org.jsoftware.config.dialect.PatchExecutionResult;
import org.jsoftware.impl.extension.Extension;
import org.jsoftware.impl.statements.DisallowedSqlPatchStatement;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

public class DbManager {
	private ConfigurationEntry ce;
	private Dialect dialect;
	private Connection c;
	private final Log log = LogFactory.getInstance();
	private List<Extension> extensions;
	
	
	public DbManager(ConfigurationEntry ce) throws SQLException {
		this.ce = ce;
		this.extensions = new LinkedList<Extension>(ce.getExtensions());
		this.dialect = ce.getDialect();
		try {
			Class.forName(ce.getDriverClass()).newInstance();
		} catch(Exception ex) {
			throw new SQLException("Could not load driver class - " + ce.getDriverClass());
		}
	}
	
	public void init(DbManagerPasswordCallback dbManagerPasswordCallback) throws SQLException {
		Connection con = null;
		int tryNo = 0;
		String password = ce.getPassword();
		do {
			try {
				con = DriverManager.getConnection(ce.getJdbcUri(), ce.getUser(), password);
			} catch (SQLException e) {
				password = dbManagerPasswordCallback.getPassword(e, tryNo, ce);
				tryNo++;
				continue;
			}
			break;
		} while (true);
		if (con != null) {			
			dialect.checkAndCreateStruct(con);
			c = con;
		}
	}
	
	public Connection getConnection() {
		if (c == null) {
			throw new IllegalStateException("Invoke init method first.");
		}
		return c;
	}

	public void updateStateObject(Patch p) throws SQLException {
		PreparedStatement ps = c.prepareStatement("SELECT patch_db_date FROM "+ dialect.getDbPatchTableName() +" WHERE patch_name=?");
		ps.setString(1, p.getName());
		ResultSet rs = ps.executeQuery();
		try {
			if (rs.next()) {
				Date d = rs.getDate(1);
				if (d != null) {
					p.setDbState(DbState.COMMITED);
					p.setDbDate(d);
				} else {
					p.setDbState(DbState.IN_PROGRES);
				}
			} else {
				p.setDbState(DbState.NOT_AVAILABLE);		
			}
		} finally {
			rs.close();
			ps.close();
		}
	}

	public void apply(final Patch p) throws SQLException {
		PatchStatement psErr = null;
		try {
			log.info("Patch " + p.getName());
			dialect.savePatchInfoPrepare(c, p);
			invokeExtensions("beforePatch", new ExtensionMethodInvokeCallback() {
				public void invokeOn(Extension extension) throws Exception {
					extension.beforePatch(c, p);
				}
			});
			for(final PatchStatement ps : ce.getPatchParser().parse(p, ce).getStatements()) {
				if (ps.isDisplayable()) {
					log.debug(ps.toString());
				}
				if (ps.isExecutable()) {
					psErr = ps;
					invokeExtensions("beforePatchStatement", new ExtensionMethodInvokeCallback() {
						public void invokeOn(Extension extension) throws Exception {
							extension.beforePatchStatement(c, p, ps);
						}
					});
					final PatchExecutionResult result = dialect.executeStatement(c, ps);
					invokeExtensions("afterPatchStatement", new ExtensionMethodInvokeCallback() {
						public void invokeOn(Extension extension) throws Exception {
							extension.afterPatchStatement(c, p, result);
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
			psErr = null;
			dialect.savePatchInfoFinal(c, p);
			invokeExtensions("afterPatchComplete", new ExtensionMethodInvokeCallback() {
				public void invokeOn(Extension extension) throws Exception {
					extension.afterPatch(c, p, null);
				}
			});
			c.commit();
			log.debug("Patch " + p.getName() + " commited.");
		} catch (Exception e) {
			if (psErr != null) {
				log.warn("Query execution problem \"" + psErr + "\" - " + e);
			}
			log.warn("Patch " + p.getName() + " execution error!"  + e);
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
	

	public void updateStateObjectAll(Collection<Patch> patches) throws SQLException {
		for(Patch p : patches) {
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
		try { c.rollback(); } catch (SQLException e) { /* ignore */	}
		invokeExtensions("afterPatching", new ExtensionMethodInvokeCallback() {
			public void invokeOn(Extension extension) throws Exception {
				extension.afterPatching(c);
			}
		});
		dialect.releaseLock(c);
	}


	public void dispose() {
		CloseUtil.close(c);
	}
	
	private void invokeExtensions(String method, ExtensionMethodInvokeCallback cb) {
		for(Extension extension : extensions) {
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