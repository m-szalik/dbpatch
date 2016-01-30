package org.jsoftware.dbpatch.impl;

import org.jsoftware.dbpatch.impl.statements.CommentPatchStatement;
import org.jsoftware.dbpatch.impl.statements.DisallowedSqlPatchStatement;
import org.jsoftware.dbpatch.impl.statements.SqlPatchStatement;

/**
 * Represents single SQL statements
 *
 * @see CommentPatchStatement
 * @see SqlPatchStatement
 * @see DisallowedSqlPatchStatement
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
