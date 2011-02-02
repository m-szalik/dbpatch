package org.jsoftware.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.config.Patch;
import org.jsoftware.config.Patch.DbState;
import org.jsoftware.config.dialect.Dialect;
import org.jsoftware.impl.extension.Extension;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

public class DbManager {
	private ConfigurationEntry ce;
	private Dialect dialect;
	private Connection c;
	private final Log log = LogFactory.getInstance();

	
	public DbManager(ConfigurationEntry ce) throws SQLException {
		this.ce = ce;
		this.dialect = ce.getDialect();
		try {
			Class.forName(ce.getDriverClass()).newInstance();
		} catch(Exception ex) {
			throw new SQLException("Could not load driver class - " + ce.getDriverClass());
		}
	    c = DriverManager.getConnection(ce.getJdbcUri(), ce.getUser(), ce.getPassword());
	    dialect.checkAndCreateStruct(c);
	}
	
	public Connection getConnection() {
		return c;
	}

	public void updateStateObject(Patch p) throws SQLException {
		PreparedStatement ps = c.prepareStatement("SELECT patch_db_date FROM "+ dialect.getDbPatchTableName() +" WHERE patch_name=?");
		ps.setString(1, p.getName());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Date d = rs.getDate(1);
			rs.close();
			ps.close();
			if (d != null) {
				p.setDbState(DbState.COMMITED);
				p.setDbDate(d);
			} else {
				p.setDbState(DbState.IN_PROGRES);
			}
		} else {
			p.setDbState(DbState.NOT_AVAILABLE);		
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
					dialect.executeStatement(c, ps);
					invokeExtensions("afterPatchStatement", new ExtensionMethodInvokeCallback() {
						public void invokeOn(Extension extension) throws Exception {
							extension.afterPatchStatement(c, p, ps);
						}
					});
				}
			}
			psErr = null;
			dialect.savePatchInfoFinal(c, p);
			invokeExtensions("afterPatchComplete", new ExtensionMethodInvokeCallback() {
				public void invokeOn(Extension extension) throws Exception {
					extension.afterPatchComplete(c, p);
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
					extension.afterPatchError(c, p, ex.getNextException());
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
		try { c.rollback(); } catch (SQLException e) {	}
		invokeExtensions("afterPatching", new ExtensionMethodInvokeCallback() {
			public void invokeOn(Extension extension) throws Exception {
				extension.afterPatching(c);
			}
		});
		dialect.releaseLock(c);
	}


	public void dispose() {
		try {
			c.close();
		} catch (SQLException e) {	}
	}
	
	private void invokeExtensions(String method, ExtensionMethodInvokeCallback cb) {
		for(Extension extension : ce.getExtensions()) {
			log.debug("Invoke method (" + method + ") on " + extension.getClass());
			try {
				cb.invokeOn(extension);
			} catch (Exception e) {
				log.warn("Invoke method (" + method + ") on " + extension.getClass() + " throws " + e, e);
			}
		}
	}
}

interface ExtensionMethodInvokeCallback {
	void invokeOn(Extension extension) throws Exception;
}