package org.jsoftware.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.jsoftware.config.ConfigurationEntry;

public class ConsoleDbManagerPasswordCallback extends AbstractDbManagerPasswordCallback {

	
	@Override
	protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
		if (lastSqlException != null) {
			System.out.println(lastSqlException.getLocalizedMessage());
		}
		System.out.print("Enter password for " + configurationEntry.getUser() + " to " + configurationEntry.getJdbcUri() + ":");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String password = br.readLine();
			return password;
		} catch (IOException e) {
			throw new RuntimeException("Cannot read password from console!", e);
		} finally {
			System.out.println();
		}
	}

}
