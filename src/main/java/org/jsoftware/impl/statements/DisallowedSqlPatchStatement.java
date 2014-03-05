package org.jsoftware.impl.statements;

import org.jsoftware.impl.PatchStatement;

/**
 * Disallowed statements
 *
 * @author szalik
 */
public class DisallowedSqlPatchStatement implements PatchStatement {
    protected final String sql;

    public DisallowedSqlPatchStatement(String sql) {
        this.sql = sql;
    }

    public boolean isDisplayable() {
        return true;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + sql;
    }

    public boolean isExecutable() {
        return false;
    }

    public String getCode() {
        return sql;
    }

}
