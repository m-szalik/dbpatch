package org.jsoftware.impl;

/**
 * Represents single SQL statements
 * @see org.jsoftware.impl.statements.CommentPatchStatement
 * @see org.jsoftware.impl.statements.SqlPatchStatement
 * @see org.jsoftware.impl.statements.DisallowedSqlPatchStatement
 */
public interface PatchStatement {

    /**
     * @return true if can be displayed eg in logs
     */
    boolean isDisplayable();

    /**
     * @return true if can be executed (false for comments)
     */
    boolean isExecutable();

    /**
     * @return SQL code
     */
    String getCode();

}
