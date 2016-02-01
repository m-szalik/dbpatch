package org.jsoftware.dbpatch.impl.extension;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.Patch;
import org.jsoftware.dbpatch.config.RollbackPatch;
import org.jsoftware.dbpatch.config.dialect.PatchExecutionResult;
import org.jsoftware.dbpatch.impl.PatchStatement;

import java.sql.Connection;
import java.sql.SQLException;

public interface Extension {

    /**
     * @param connection connection
     * Before patching procedure starts
     */
    void beforePatching(Connection connection);

    /**
     * @param connection connection
     * After patching process ends
     */
    void afterPatching(Connection connection);

    /**
     * @param connection connection
     * @param patch patch to apply
     * Before the patch is executed
     */
    void beforePatch(Connection connection, Patch patch);

    /**
     * After the patch is executed
     * @param connection connection
     * @param patch patch to apply
     * @param ex execution exception - null if successful execution
     * @throws SQLException sql problem
     */
    void afterPatch(Connection connection, Patch patch, Exception ex) throws SQLException;

    /**
     * Before patch's statement is executed
     * @param connection connection
     * @param patch patch
     * @param statement sql statement
     */
    void beforePatchStatement(Connection connection, AbstractPatch patch, PatchStatement statement);

    /**
     * After patch's statement is executed
     * @param connection connection
     * @param patch patch
     * @param result patching result
     */
    void afterPatchStatement(Connection connection, AbstractPatch patch, PatchExecutionResult result);

    /**
     * Before the patch rollback is executed
     * @param connection connection
     * @param patch patch to rollback
     */
    void beforeRollbackPatch(Connection connection, RollbackPatch patch);

    /**
     * After the patch rollback is executed
     * @param connection connection
     * @param patch patch that was rollbacked
     * @param ex exception if an error occurred
     * @throws SQLException sql problem
     */
    void afterRollbackPatch(Connection connection, RollbackPatch patch, Exception ex) throws SQLException;

}

