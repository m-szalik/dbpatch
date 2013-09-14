package org.jsoftware.impl;

import org.jsoftware.config.ConfigurationEntry;

import java.sql.SQLException;

public abstract class AbstractDbManagerPasswordCallback implements DbManagerPasswordCallback {
	private final static int MAX_TRIES = 3;
	
	public String getPassword(SQLException sqlException, int tryNo, ConfigurationEntry configurationEntry) throws SQLException {
		if (tryNo >= MAX_TRIES) {
			SQLException ex = new SQLException("Cannot connect to " + configurationEntry.getJdbcUri() + " user:" + configurationEntry.getUser());
			ex.initCause(sqlException);
			throw ex;
		}
		if (! configurationEntry.isInteractivePasswordAllowed()) {
			throw new SQLException("Cannot read password iteratively!");
		}
		return getPassword(sqlException, configurationEntry);
	}

	
	protected abstract String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry);

		
}
