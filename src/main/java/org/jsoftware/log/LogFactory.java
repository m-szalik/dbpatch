package org.jsoftware.log;

import java.io.PrintStream;

/**
 * Logger Factory
 *
 * @author szalik
 */
public class LogFactory {
    private static Log instance;

    public static void initLocal() {
        LogLocalImpl l = new LogLocalImpl();
        l.currentLevel = Level.DEBUG;
        String logStr = System.getProperty("maven.dbpatch.log");
        if (logStr != null && logStr.trim().length() > 0) {
            logStr = logStr.trim().toUpperCase();
            l.currentLevel = Level.valueOf(logStr);
        }
        instance = l;
    }

    public static void initMaven(final org.apache.maven.plugin.logging.Log mLog) {
        instance = new Log() {

            @Override
            public void trace(String msg, Throwable e) {
                if (e == null) {
                    mLog.debug(msg);
                } else {
                    mLog.debug(msg, e);
                }
            }

            public void warn(String msg) {
                mLog.warn(msg);
            }

            public void info(String msg) {
                mLog.info(msg);
            }

            public void fatal(String msg) {
                mLog.error(msg);
            }

            public void debug(String msg) {
                mLog.debug(msg);
            }

            public void warn(String msg, Throwable e) {
                mLog.warn(msg, e);
            }

            public void fatal(String msg, Throwable e) {
                mLog.error(msg, e);
            }
        };
    }

    private LogFactory() {
    }


    public static Log getInstance() {
        if (instance == null) {
            initLocal();
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

    @Override
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
