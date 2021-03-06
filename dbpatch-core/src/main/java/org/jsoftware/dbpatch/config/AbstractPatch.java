package org.jsoftware.dbpatch.config;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * Basic patch information.
 * <ul>
 * <li>file name</li>
 * <li>statements count</li>
 * <li>database state and apply date</li>
 * </ul>
 */
public abstract class AbstractPatch implements Serializable {
    private static final long serialVersionUID = 4178101927323891639L;
    private String name;
    private int statementCount = -1;
    private File file;

    public enum DbState {
        COMMITTED, IN_PROGRESS, NOT_AVAILABLE
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatementCount() {
        return statementCount;
    }

    public void setStatementCount(int statementCount) {
        this.statementCount = statementCount;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public abstract void setDbDate(Date dbDate);

    public abstract void setDbState(DbState dbState);

    public abstract Date getDbDate();

    public abstract DbState getDbState();

    public abstract boolean canApply();

    public String toString() {
        return super.toString() + "-" + name;
    }

    public static String normalizeName(String name) {
        String nameLC = name.toLowerCase();
        while (nameLC.endsWith(".sql") || nameLC.endsWith(".undo") || nameLC.endsWith(".rollback")) {
            int dot = nameLC.lastIndexOf('.');
            nameLC = nameLC.substring(0, dot);
        }
        return name.substring(0, nameLC.length());
    }

}
