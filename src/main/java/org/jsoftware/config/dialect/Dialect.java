package org.jsoftware.config.dialect;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.Patch;
import org.jsoftware.config.RollbackPatch;
import org.jsoftware.impl.PatchStatement;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Consider to extend org.jsoftware.config.dialect.DefaultDialect instead creating new one from scratch.
 *
 * @author szalik
 */
public interface Dialect extends Serializable {
    static final String DBPATCH_TABLE_NAME = "db_patches";

    /**
     * It is recommended to be #DBPATCH_TABLE_NAME but can be customized if necessary
     *
     * @return name of dbPatch info table
     */
    String getDbPatchTableName();

    /**
     * Make lock on database to disallow other dbPatch instances to do DDL (data description language) changes.
     *
     * @throws SQLException
     * @see #releaseLock(java.sql.Connection)
     */
    void lock(Connection con, long timeout) throws SQLException;

    /**
     * Unlock database
     *
     * @throws SQLException
     * @see #lock(java.sql.Connection, long)
     */
    void releaseLock(Connection con) throws SQLException;

    /**
     * Check if there is dbPatch table if not create it
     *
     * @throws SQLException
     * @see #getDbPatchTableName()
     */
    void checkAndCreateStruct(Connection con) throws SQLException;

    /**
     * Execute dbPatch statement
     */
    PatchExecutionResult executeStatement(Connection con, PatchStatement ps);

    /**
     * Save patch information after successful patch apply.
     * A field "patch_db_date" must be set to current date
     *
     * @throws SQLException
     */
    void savePatchInfoFinal(Connection con, Patch patch) throws SQLException;

    /**
     * Save patch information before patch apply.
     * A field "patch_db_date" must be set to null
     *
     * @throws SQLException
     */
    void savePatchInfoPrepare(Connection con, Patch patch) throws SQLException;

    /**
     * Get database now timestamp
     */
    Timestamp getNow(Connection con) throws SQLException;

    void removePatchInfo(Connection c, RollbackPatch p) throws SQLException;

    boolean checkIfPatchIsCommitted(Connection c, AbstractPatch patch) throws SQLException;

}
