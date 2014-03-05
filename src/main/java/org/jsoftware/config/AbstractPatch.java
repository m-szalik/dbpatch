package org.jsoftware.config;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * Basic patch information.
 * <ul>
 *     <li>file name</li>
 *     <li>statements count</li>
 *     <li>database state and apply date</li>
 * </ul>
 */
public abstract class AbstractPatch implements Serializable {
    private static final long serialVersionUID = 4178101927323891639L;

    public static String normalizeName(String name) {
        String nameLC = name.toLowerCase();
        while (nameLC.endsWith(".sql") || nameLC.endsWith(".undo") || nameLC.endsWith(".rollback")) {
            int dot = nameLC.lastIndexOf('.');
            nameLC = nameLC.substring(0, dot);
        }
        return name.substring(0, nameLC.length());
    }

    public enum DbState {
        COMMITTED, IN_PROGRESS, NOT_AVAILABLE
    }

    private String name;
    private int statementCount = -1;
    private File file;
    private Date dbDate;
    private DbState dbState;

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

    public void setDbDate(Date dbDate) {
        this.dbDate = dbDate;
    }

    public void setDbState(DbState dbState) {
        this.dbState = dbState;
    }

    public Date getDbDate() {
        return dbDate;
    }

    public DbState getDbState() {
        return dbState;
    }

    public abstract boolean canApply();

    @Override
    public String toString() {
        return super.toString() + "-" + name;
    }
}
