package org.jsoftware.impl;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.Patch;
import org.jsoftware.config.RollbackPatch;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class JTablePatchStateRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = null;
        Color color = null;
        if (value instanceof Patch) {
			Patch p = (Patch) value;
            String text = "";
			if (p.getDbState() == AbstractPatch.DbState.IN_PROGRESS) {
				text = Messages.msg("table.patches.state.inProgress");
			}
			if (p.getDbState() == AbstractPatch.DbState.COMMITTED) {
                text = Messages.msg("table.patches.state.committed");
                color = Color.GREEN;
			}
			if (p.getDbState() == AbstractPatch.DbState.NOT_AVAILABLE) {
				if (p.canApply()) {
                    text = Messages.msg("table.patches.state.missing");
				} else {
					text = Messages.msg("table.patches.state.empty");
				}
			}
            component = super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
		}
        if (value instanceof RollbackPatch) {
            String text = "";
            RollbackPatch rp = (RollbackPatch) value;
            if (rp.isMissing()) {
                text = Messages.msg("table.patches.state.rollback.missing");
                color = Color.RED;
            }
            component = super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
        }
        if (value instanceof JButton) {
            component = (JComponent) value;
        }
        if (component == null) {
            component = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        }
        if (color != null) {
            component.setForeground(color);
        }
        return component;
    }

}
