package org.jsoftware.dbpatch.impl.gui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    private static Messages INSTANCE;
    private ResourceBundle bundle;
    private Locale locale;

    public static void init() {
        Messages m = new Messages();
        m.locale = Locale.getDefault();
        m.bundle = ResourceBundle.getBundle("messages");
        INSTANCE = m;
    }


    public String message(String key, Object... args) {
        MessageFormat messageFormat = new MessageFormat(bundle.getString(key), locale);
        return messageFormat.format(args);
    }

    public static String msg(String key, Object... args) {
        return INSTANCE.message(key, args);
    }
}
