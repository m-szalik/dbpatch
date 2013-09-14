package org.jsoftware.impl;

import org.jsoftware.config.Patch;
import org.jsoftware.config.Patch.DbState;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTablePatchStateRenderer implements TableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Patch) {
			Patch p = (Patch) value;
			JLabel o = new JLabel("?");
			o.setBackground(Color.LIGHT_GRAY);
			if (p.getDbState() == DbState.IN_PROGRESS) {
				o = new JLabel(Messages.msg("table.patches.state.inProgress"));
				o.setBackground(Color.YELLOW);
			}
			if (p.getDbState() == DbState.COMMITTED) {
				o = new JLabel(Messages.msg("table.patches.state.committed"));
				o.setBackground(Color.GREEN);
			}
			if (p.getDbState() == DbState.NOT_AVAILABLE) {
				if (p.canApply()) {
					return new JButton(Messages.msg("table.patches.state.patchItBtn"));
				} else {
					o.setText(Messages.msg("table.patches.state.empty"));
				}
			}
			return o;
		}
		return new JLabel();
	}

}
