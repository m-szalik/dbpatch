package org.jsoftware.dbpatch.impl.gui;

import org.apache.commons.io.IOUtils;
import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.impl.AbstractDbManagerCredentialsCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.SQLException;

/**
 * If missing password ask via command line console
 */
public class ConsoleDbManagerPasswordCallback extends AbstractDbManagerCredentialsCallback {

    public String getUsername(ConfigurationEntry configurationEntry) throws SQLException {
        String defaultUsername = System.getProperty("user.name");
        System.out.print("Enter username for " + configurationEntry.getJdbcUri() + (defaultUsername != null ? "[" + defaultUsername + "]" : "") + ":");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset().name()));
            String str = br.readLine();
            if (str == null || str.length() == 0) {
                str = defaultUsername;
            }
            return str;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read username from console!", e);
        } finally {
            IOUtils.closeQuietly(br);
            System.out.println();
        }
    }


    protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
        if (lastSqlException != null) {
            System.out.println(lastSqlException.getLocalizedMessage());
        }
        System.out.print("Enter password for " + configurationEntry.getUser() + " to " + configurationEntry.getJdbcUri() + ":");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read password from console!", e);
        } finally {
            System.out.println();
        }
    }

}
