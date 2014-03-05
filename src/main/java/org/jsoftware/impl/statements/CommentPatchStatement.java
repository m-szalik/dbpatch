package org.jsoftware.impl.statements;

import org.jsoftware.impl.PatchStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQL comment
 */
public class CommentPatchStatement implements PatchStatement {
    protected String comment;

    public CommentPatchStatement(String comment) {
        this.comment = comment;
    }

    public boolean isDisplayable() {
        return true;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + comment;
    }

    public String getCode() {
        return comment;
    }

    public boolean isExecutable() {
        return false;
    }

    public int execute(Connection con) throws SQLException {
        return 0;
    }

}
