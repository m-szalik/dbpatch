package org.jsoftware.impl;

import org.jsoftware.config.AbstractPatch;

import javax.swing.*;

/**
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
