package org.jsoftware.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jsoftware.config.ConfigurationEntry;

public class InteractivePanel {
	
	private Collection<ConfigurationEntry> conf;
	private JDesktopPane desktop;
	private Map<String,JInternalFrame> frames = new HashMap();
	private boolean active;
	
	public InteractivePanel(Collection<ConfigurationEntry> conf) {
		this.conf = conf;
	}
	
	public void join() {
		while (active) {
			try { Thread.sleep(100); } catch (InterruptedException e) {		}
		}
	}
	
	private void open(String id) throws SQLException, IOException {
		JInternalFrame fr = null;
		if (frames.get(id) != null) {
			fr = frames.get(id);
			if (fr.isClosed()) fr = null;
		}
		if (fr == null) {
			for(ConfigurationEntry ce : conf) {
				if (ce.getId().equals(id)) {
					fr = new DbPatchInternalFrame(ce);
					frames.put(id, fr);
					desktop.add(fr);
					try {	fr.setMaximum(true);	} catch (PropertyVetoException e) {		}
				}
			}
		}
		
		try {
			if (fr != null) fr.setSelected(true);
		} catch (PropertyVetoException e) {	}
	}
	
	public void start() {
		final JFrame frame = new JFrame("DbPatch");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menu = new JMenuBar();
		JMenu connections = new JMenu("connections");
		for(final ConfigurationEntry ce : conf) {
			JMenuItem mi = new JMenuItem(ce.getId());
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						open(ce.getId());
					} catch (Exception e1) {
						throw new RuntimeException(e1);
					}
				}
			});
			connections.add(mi);
		}
		JMenu dbPatch = new JMenu("dbPatch");
		menu.add(dbPatch);
		JMenuItem mi = new JMenuItem("EcontentPanexit");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			} });
		dbPatch.add(mi);
		menu.add(connections);
		frame.setJMenuBar(menu);
		desktop = new JDesktopPane();
		frame.setContentPane(desktop);
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {	
			}
			public void windowClosed(WindowEvent e) {
				active = false;
			}
			public void windowActivated(WindowEvent e) {
			}
		});
		active = true;
	}
}
