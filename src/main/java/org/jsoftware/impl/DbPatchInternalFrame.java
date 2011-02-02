package org.jsoftware.impl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.config.Patch;

@SuppressWarnings("serial")
public class DbPatchInternalFrame extends JInternalFrame implements MouseListener {

	private List<Patch> patches;
	private DbManager dbManager;
	
	public DbPatchInternalFrame(final ConfigurationEntry ce) throws SQLException, IOException {
		super("DbPatch - " + ce.getId(), true, true, true);
		dbManager = new DbManager(ce);
		patches = ce.getPatchScaner().scan(new File("."), ce.getPatchDirs().split(","));
		final String[] columnModel = new String[] { "patch name", "patch date", "patch statements", "patch size", "state" };
		AbstractTableModel tableModel = new AbstractTableModel() {
			DateFormat df = DateFormat.getInstance();
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (rowIndex >= patches.size()) return null;
				Patch p = patches.get(rowIndex);
				switch (columnIndex) {
				case 0:
					return p.getName();
				case 1:
					return df.format(new Date(p.getFile().lastModified()));
				case 2:
					return p.getStatementCount() < 0 ? "" : p.getStatementCount();
				case 3:
					long len = p.getFile().length();
					return len > 102400 ?(len / 1024) + "kB" : len + "B";
				case 4:
					return p;
				default:
					return null;
				}
			}
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 4 ? Patch.class : super.getColumnClass(columnIndex);
			}
			public int getRowCount() {
				return patches.size();
			}
			public int getColumnCount() {
				return columnModel.length;
			}
			public String getColumnName(int column) {
				return columnModel[column];
			}
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == 4;
			}
		};
		final JTable table = new JTable(tableModel);
//		table.setFillsViewportHeight(true);
		table.setDefaultRenderer(Patch.class, new JTablePatchStateRenderer());
		table.addMouseListener(this);
		JScrollPane scrollPane = new JScrollPane(table);
		setContentPane(scrollPane);
		pack();
		setVisible(true);
		
		for(int i=0; i<patches.size(); i++) {
			Patch p = patches.get(i);
			try {
				int executableCount = ce.getPatchParser().parse(new FileInputStream(p.getFile()), ce).executableCount();
				p.setStatementCount(executableCount);
				dbManager.updateStateObject(p);
				table.setValueAt(p, i, 4);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		table.repaint();

	}
	
	public void mouseClicked(MouseEvent e) {
		JTable target = (JTable) e.getSource();
		int row = target.getSelectedRow();
		int col = target.getSelectedColumn();
		Object obj = target.getValueAt(row, col);
		if (obj instanceof Patch) {
			Patch patch = (Patch) obj;
			if (patch.canApply()) {
				try {
					dbManager.startExecution();
					dbManager.apply(patch);
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				} finally {
					try {	dbManager.endExecution();	} catch (SQLException e1) {	}
				}
				return;
			}
		}
		if (e.getClickCount() == 2) {
			// TODO invoke sql text editor
		}
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {	
	}
}
