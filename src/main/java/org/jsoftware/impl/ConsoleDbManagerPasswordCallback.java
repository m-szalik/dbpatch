package org.jsoftware.impl;

import org.jsoftware.config.ConfigurationEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ConsoleDbManagerPasswordCallback extends AbstractDbManagerCredentialsCallback {


    @Override
    public String getUsername(ConfigurationEntry configurationEntry) throws SQLException {
        String defaultUsername = System.getProperty("user.name");
        System.out.print("Enter username for " + configurationEntry.getJdbcUri() + (defaultUsername != null ? "[" + defaultUsername + "]" : "") + ":");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String str = br.readLine();
            if (str == null || str.length() == 0) {
                str = defaultUsername;
            }
            return str;
        } catch (IOException e) {
            throw new RuntimeException("Cannot read username from console!", e);
        } finally {
            System.out.println();
        }
    }

    @Override
    protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
        if (lastSqlException != null) {
            System.out.println(lastSqlException.getLocalizedMessage());
        }
        System.out.print("Enter password for " + configurationEntry.getUser() + " to " + configurationEntry.getJdbcUri() + ":");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read password from console!", e);
        } finally {
            System.out.println();
        }
    }

}
