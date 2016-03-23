package org.jsoftware.dbpatch.impl.gui;

import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.impl.AbstractDbManagerCredentialsCallback;

import javax.swing.*;
import java.sql.SQLException;

/**
 * GUI
 * If password missing ask using GUI dialogue.
 */
public class SwingDbManagerPasswordCallback extends AbstractDbManagerCredentialsCallback {
    private final JInternalFrame frame;

    public SwingDbManagerPasswordCallback(JInternalFrame frame) {
        this.frame = frame;
    }


    public String getUsername(ConfigurationEntry configurationEntry) throws SQLException {
        String defaultUsername = System.getProperty("user.name");
        return (String) JOptionPane.showInputDialog(frame, "Enter username: ", configurationEntry.getJdbcUri(), JOptionPane.QUESTION_MESSAGE, null, null, defaultUsername);
    }


    protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
        return JOptionPane.showInputDialog(frame, "Enter " + configurationEntry.getUser() + "'s password: ", configurationEntry.getJdbcUri(), JOptionPane.QUESTION_MESSAGE);
    }


}
