package org.jsoftware.impl;

import org.jsoftware.config.ConfigurationEntry;

import javax.swing.*;
import java.sql.SQLException;

public class SwingDbManagerPasswordCallback extends AbstractDbManagerCredentialsCallback {
    private JInternalFrame frame;

    public SwingDbManagerPasswordCallback(JInternalFrame frame) {
        this.frame = frame;
    }

    @Override
    public String getUsername(ConfigurationEntry configurationEntry) throws SQLException {
        String defaultUsername = System.getProperty("user.name");
        return (String) JOptionPane.showInputDialog(frame, "Enter username: ", configurationEntry.getJdbcUri(), JOptionPane.QUESTION_MESSAGE, null, null, defaultUsername);
    }

    @Override
    protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
        return JOptionPane.showInputDialog(frame, "Enter " + configurationEntry.getUser() + "'s password: ", configurationEntry.getJdbcUri(), JOptionPane.QUESTION_MESSAGE);
    }


}
