package org.jsoftware.impl;

import java.sql.SQLException;

import org.jsoftware.config.ConfigurationEntry;

public interface DbManagerPasswordCallback {

	String getPassword(SQLException sqlException, int tryNo, ConfigurationEntry configurationEntry) throws SQLException;
	
}
