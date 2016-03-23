package org.jsoftware.dbpatch.impl.gui;

import org.jsoftware.dbpatch.config.AbstractPatch;

import javax.swing.*;

/**
 * GUI button
 *
 * @author szalik
 */
public class JPatchButton extends JButton {

    private final AbstractPatch patch;

    public JPatchButton(String msg, AbstractPatch p) {
        super(msg);
        this.patch = p;
    }

    public AbstractPatch getPatch() {
        return patch;
    }
}
