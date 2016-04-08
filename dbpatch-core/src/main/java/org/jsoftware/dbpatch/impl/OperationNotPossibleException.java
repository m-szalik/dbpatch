package org.jsoftware.dbpatch.impl;

/**
 * @author szalik
 */
public class OperationNotPossibleException extends RuntimeException {
    public OperationNotPossibleException(String msg, Exception ex) {
        super(msg);
        if (ex != null) {
            initCause(ex);
        }
    }

    public OperationNotPossibleException(Exception ex) {
        if (ex != null) {
            initCause(ex);
        }
    }
}
