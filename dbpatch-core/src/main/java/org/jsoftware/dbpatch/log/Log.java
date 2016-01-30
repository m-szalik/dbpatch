package org.jsoftware.dbpatch.log;

/**
 * DbPatch logger facade
 *
 * @author szalik
 */
public interface Log {

    void trace(String msg, Throwable e);

    void debug(String msg);

    void info(String msg);

    void warn(String msg);

    void fatal(String msg);

    void warn(String msg, Throwable e);

    void fatal(String msg, Throwable e);
}
