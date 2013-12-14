package org.jsoftware.impl;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.Patch;
import org.jsoftware.config.RollbackPatch;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTablePatchStateRenderer implements TableCellRenderer {
    private static final JLabel EMPTY = new JLabel();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Patch) {
			Patch p = (Patch) value;
			JLabel o = new JLabel("?");
			o.setBackground(Color.LIGHT_GRAY);
			if (p.getDbState() == AbstractPatch.DbState.IN_PROGRESS) {
				o = new JLabel(Messages.msg("table.patches.state.inProgress"));
				o.setBackground(Color.YELLOW);
			}
			if (p.getDbState() == AbstractPatch.DbState.COMMITTED) {
				o = new JLabel(Messages.msg("table.patches.state.committed"));
				o.setBackground(Color.GREEN);
			}
			if (p.getDbState() == AbstractPatch.DbState.NOT_AVAILABLE) {
				if (p.canApply()) {
                    o = new JLabel(Messages.msg("table.patches.state.missing"));
                    o.setBackground(Color.GRAY);
				} else {
					o.setText(Messages.msg("table.patches.state.empty"));
				}
			}
			return o;
		}
        if (value instanceof RollbackPatch) {
            RollbackPatch rp = (RollbackPatch) value;
            if (rp.isMissing()) {
                return new JLabel(Messages.msg("table.patches.state.rollback.missing"));
            }
            return new JLabel();
        }
        if (value instanceof JButton) {
            return (JButton) value;
        }
		return EMPTY;
	}

}
