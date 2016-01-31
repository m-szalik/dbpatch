package org.jsoftware.dbpatch.log;

import org.jsoftware.dbpatch.config.EnvSettings;

import java.io.PrintStream;

/**
 * Logger Factory
 *
 * @author szalik
 */
public class LogFactory {
    private static Log instance;

    public static void initLocal(EnvSettings envSettings) {
        LogLocalImpl l = new LogLocalImpl();
        l.currentLevel = Level.DEBUG;
        String logStr = System.getProperty(envSettings.getLogLevel());
        if (logStr != null && logStr.trim().length() > 0) {
            logStr = logStr.trim().toUpperCase();
            l.currentLevel = Level.valueOf(logStr);
        }
        instance = l;
    }

    public static void init(Log log) {
        instance = log;
    }

    private LogFactory() {
    }


    public static Log getInstance() {
        if (instance == null) {
            initLocal(EnvSettings.standalone());
        }
        return instance;
    }

}

/**
 * Implementation that uses stdOut as log output stream
 *
 * @author szalik
 */
class LogLocalImpl implements Log {
    Level currentLevel;

    public void trace(String msg, Throwable e) {
        log(Level.TRACE, msg, e);
    }

    public void debug(String msg) {
        log(Level.DEBUG, msg, null);
    }

    public void info(String msg) {
        log(Level.INFO, msg, null);
    }

    public void warn(String msg) {
        log(Level.WARN, msg, null);
    }

    public void fatal(String msg) {
        log(Level.FATAL, msg, null);
    }

    public void warn(String msg, Throwable e) {
        log(Level.WARN, msg, e);
    }

    public void fatal(String msg, Throwable e) {
        log(Level.FATAL, msg, e);
    }

    private void log(Level lev, String msg, Throwable throwable) {
        PrintStream stream = System.out;
        if (lev.getPriority() >= currentLevel.getPriority()) {
            stream.println(lev.name() + " " + msg);
            if (throwable != null) {
                throwable.printStackTrace(stream);
            }
        }
    }
}
