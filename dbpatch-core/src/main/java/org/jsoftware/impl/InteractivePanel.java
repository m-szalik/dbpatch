package org.jsoftware.impl;

import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.config.EnvSettings;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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

public class InteractivePanel {
    private final Log log = LogFactory.getInstance();
    private final Collection<ConfigurationEntry> conf;
    private JDesktopPane desktop;
    private final Map<String, JInternalFrame> frames = new HashMap<String, JInternalFrame>();
    private boolean active;
    private Image defaultIcon;


    public InteractivePanel(Collection<ConfigurationEntry> conf) {
        Messages.init();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                log.trace("Cannot set look and feel", e1);
            }
        }
        try {
            defaultIcon = ImageIO.read(getClass().getResource("/icon.png"));
        } catch (IOException e) {
            log.trace("Cannot load app icon", e);
        }
        this.conf = conf;
    }

    public void join() {
        while (active) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                active = false;
            }
        }
    }

    private void open(String id) throws SQLException, IOException, DuplicatePatchNameException {
        JInternalFrame fr = null;
        if (frames.get(id) != null) {
            fr = frames.get(id);
            if (fr.isClosed()) {
                fr = null;
            }
        }
        if (fr == null) {
            for (ConfigurationEntry ce : conf) {
                if (ce.getId().equals(id)) {
                    fr = new DbPatchInternalFrame(ce);
                    if (defaultIcon != null) {
                        fr.setFrameIcon(new ImageIcon(defaultIcon));
                    }
                    frames.put(id, fr);
                    desktop.add(fr);
                    try {
                        fr.setMaximum(true);
                    } catch (PropertyVetoException e) {
                        log.trace("Unable to set frame to maximum", e);
                    }
                }
            }
        }

        try {
            if (fr != null) {
                fr.setSelected(true);
            }
        } catch (PropertyVetoException e) {
            log.trace("Unable to set frame as selected!", e);
        }
    }

    public void start() throws IOException {
        final JFrame frame = new JFrame("DbPatch");
        if (defaultIcon != null) {
            frame.setIconImage(defaultIcon);
        }
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menu = new JMenuBar();
        JMenu connections = new JMenu(Messages.msg("menu.connections"));
        for (final ConfigurationEntry ce : conf) {
            JMenuItem mi = new JMenuItem(ce.getId());
            mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        open(ce.getId());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(frame, e1.getLocalizedMessage(), Messages.msg("error.openConnection.title", ce.getId()), JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(e1);
                    }
                }
            });
            connections.add(mi);
        }
        JMenu dbPatch = new JMenu("dbPatch");
        menu.add(dbPatch);
        JMenuItem mi;
        mi = new JMenuItem(Messages.msg("menu.exit"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
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
