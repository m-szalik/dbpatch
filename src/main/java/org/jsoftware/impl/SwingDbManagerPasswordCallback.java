package org.jsoftware.impl;

import org.jsoftware.config.ConfigurationEntry;

import javax.swing.*;
import java.sql.SQLException;

public class SwingDbManagerPasswordCallback extends AbstractDbManagerPasswordCallback {
	private JInternalFrame frame;

	public SwingDbManagerPasswordCallback(JInternalFrame frame) {
		this.frame = frame;
	}
	

	@Override
	protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
		return JOptionPane.showInputDialog(frame, "Enter " + configurationEntry.getUser() + "'s password: ", configurationEntry.getJdbcUri(), JOptionPane.QUESTION_MESSAGE);
	}

}
