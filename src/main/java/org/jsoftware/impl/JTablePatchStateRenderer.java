package org.jsoftware.impl;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.jsoftware.config.Patch;
import org.jsoftware.config.Patch.DbState;

public class JTablePatchStateRenderer implements TableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Patch) {
			Patch p = (Patch) value;
			JLabel o = new JLabel("?");
			o.setBackground(Color.LIGHT_GRAY);
			if (p.getDbState() == DbState.IN_PROGRES) {
				o = new JLabel(Messages.msg("table.patches.state.inProgress"));
				o.setBackground(Color.YELLOW);
			}
			if (p.getDbState() == DbState.COMMITED) {
				o = new JLabel(Messages.msg("table.patches.state.commited"));
				o.setBackground(Color.GREEN);
			}
			if (p.getDbState() == DbState.NOT_AVAILABLE) {
				if (p.canApply()) {
					JButton jb = new JButton(Messages.msg("table.patches.state.patchItBtn"));
					return jb;
				} else {
					o.setText(Messages.msg("table.patches.state.empty"));
				}
			}
			return o;
		}
		return new JLabel();
	}

}
