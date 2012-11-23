package org.jsoftware.impl;

import java.sql.SQLException;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.jsoftware.config.ConfigurationEntry;

public class SwingDbManagerPasswordCallback extends AbstractDbManagerPasswordCallback {
	private JInternalFrame frame;

	public SwingDbManagerPasswordCallback(JInternalFrame frame) {
		this.frame = frame;
	}
	

	@Override
	protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
		String pass = JOptionPane.showInputDialog(frame, "Enter " + configurationEntry.getUser() + "'s password: ", configurationEntry.getJdbcUri(), JOptionPane.QUESTION_MESSAGE);
		return pass;
	}

}
