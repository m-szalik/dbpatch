package org.jsoftware.impl;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.config.Patch;
import org.jsoftware.config.RollbackPatch;
import org.jsoftware.log.LogFactory;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class DbPatchInternalFrame extends JInternalFrame implements MouseListener, InternalFrameListener {
    private final static ResultDisplay resultDisplay = new ResultDisplay();
	private List<Patch> patches;
	private DbManager dbManager;
	private JTable table;
	
	
	public DbPatchInternalFrame(final ConfigurationEntry ce) throws SQLException, IOException, DuplicatePatchNameException {
		super("DbPatch - " + ce.getId(), true, true, true);
        final File dir = new File(".");
		dbManager = new DbManager(ce);
		dbManager.init(new SwingDbManagerPasswordCallback(this));
		dbManager.addExtension(resultDisplay);
		patches = ce.getPatchScanner().scan(dir, ce.getPatchDirs().split(","));
		addInternalFrameListener(this);
		final String[] columnModelKeys = new String[] { "patchName", "patchDate", "patchStatements", "patchSize", "action", "state" };
		final String[] columnModel = new String[columnModelKeys.length];
		for(int i=0; i<columnModel.length; i++) {
			columnModel[i] = Messages.msg("table.patches." + columnModelKeys[i]);
		}
		AbstractTableModel tableModel = new AbstractTableModel() {
			final DateFormat df = DateFormat.getInstance();
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (rowIndex >= patches.size()) return null;
				Patch patch = patches.get(rowIndex);
                AbstractPatch p;
                if (patch.getDbState() == AbstractPatch.DbState.COMMITTED) {
                    try {
                        File rollbackFile = ce.getPatchScanner().findRollbackFile(dir, ce.getRollbackDirs().split(","), patch);
                        int sc = ce.getPatchParser().parse(new FileInputStream(rollbackFile), ce).executableCount();
                        p = new RollbackPatch(patch, rollbackFile, sc);
                    } catch (Exception e) {
                        p = new RollbackPatch(patch);
                    }
                } else {
                    p = patch;
                }

                switch (columnIndex) {
				case 0:
					return patch.getName();
				case 1:
					return p.getFile() != null ? df.format(new Date(p.getFile().lastModified())) : "-";
				case 2:
					return p.getStatementCount() < 0 ? "" : p.getStatementCount();
				case 3:
                    if (p.getFile() != null) {
                        long len = p.getFile().length();
                        return len > 102400 ?(len / 1024) + "kB" : len + "B";
                    } else {
                        return "-";
                    }
				case 5:
					return p;
                case 4:
                    if (p.canApply()) {
                        if (p instanceof Patch) {
                            return new JPatchButton(Messages.msg("table.patches.state.patchItBtn"), p);
                        }
                        if (p instanceof RollbackPatch) {
                            return new JPatchButton(Messages.msg("table.patches.state.rollbackItBtn"), p);
                        }
                    }
                    return "";
				default:
					return null;
				}
			}
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 5) {
                    return AbstractPatch.class;
                }
                if (columnIndex == 4) {
                    return JPatchButton.class;
                }
                return super.getColumnClass(columnIndex);
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
				return columnIndex == 5;
			}
		};
		table = new JTable(tableModel);
//		table.setFillsViewportHeight(true);
        JTablePatchStateRenderer renderer = new JTablePatchStateRenderer();
        table.setDefaultRenderer(AbstractPatch.class, renderer);
        table.setDefaultRenderer(Patch.class, renderer);
        table.setDefaultRenderer(RollbackPatch.class, renderer);
        table.setDefaultRenderer(JPatchButton.class, renderer);
		table.addMouseListener(this);
		resultDisplay.setMinimumSize(new Dimension(getWidth(), 40));
		setContentPane(new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), new JScrollPane(resultDisplay)));
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
		resultDisplay.addInfo("com.patching.connection", new Object[]{ ce.getId(), ce.getJdbcUri() });
		Date dbNow = dbManager.getNow();
		resultDisplay.addInfo("com.patching.database.time", new Object[] { dbNow });
	}
	
	public void mouseClicked(MouseEvent e) {
		JTable target = (JTable) e.getSource();
		int row = target.getSelectedRow();
		int col = target.getSelectedColumn();
		Object obj = target.getValueAt(row, col);
		if (obj instanceof JPatchButton) {
            JPatchButton jPatchButton = (JPatchButton) obj;
			AbstractPatch patch = jPatchButton.getPatch();
			if (patch.canApply()) {
				try {
					dbManager.startExecution();
                    if (patch instanceof Patch) {
					    dbManager.apply((Patch) patch);
                    }
                    if (patch instanceof RollbackPatch) {
                        dbManager.rollback((RollbackPatch) patch);
                    }
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				} finally {
					try {	
						dbManager.endExecution();	
					} catch (SQLException e1) {
                        LogFactory.getInstance().fatal("Error executing SQL operation.", e1);
					}
				}
				table.updateUI();
			}
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

	// interface InternalFrameListener
	public void internalFrameActivated(InternalFrameEvent e) {	
	}
	public void internalFrameClosed(InternalFrameEvent e) {
		if (dbManager != null) {
			dbManager.dispose();
		}
	}
	public void internalFrameClosing(InternalFrameEvent e) {	
	}
	public void internalFrameDeactivated(InternalFrameEvent e) {	
	}
	public void internalFrameDeiconified(InternalFrameEvent e) {	
	}
	public void internalFrameIconified(InternalFrameEvent e) {	
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}
}
