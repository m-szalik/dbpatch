package org.jsoftware.impl;

import org.jsoftware.config.ConfigurationEntry;

import java.sql.SQLException;

public interface DbManagerCredentialsCallback {

	String getUsername(ConfigurationEntry configurationEntry) throws SQLException;

    String getPassword(SQLException sqlException, int tryNo, ConfigurationEntry configurationEntry) throws SQLException;
	
}
