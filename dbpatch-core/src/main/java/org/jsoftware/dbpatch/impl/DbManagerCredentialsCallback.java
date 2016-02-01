package org.jsoftware.dbpatch.impl;

import org.jsoftware.dbpatch.config.ConfigurationEntry;

import java.sql.SQLException;

/**
 * Decide what to do if username or password are missing or invalid
 */
public interface DbManagerCredentialsCallback {

    /**
     * Invoked when username is missing or invalid
     *
     * @param configurationEntry selected database profile
     * @return database username
     * @throws SQLException sql problem
     */
    String getUsername(ConfigurationEntry configurationEntry) throws SQLException;

    /**
     * Invoked when password is missing or invalid
     *
     * @param sqlException an sql exception
     * @param tryNo try counter
     * @param configurationEntry selected database profile
     * @return database password
     * @throws SQLException sql problem
     */
    String getPassword(SQLException sqlException, int tryNo, ConfigurationEntry configurationEntry) throws SQLException;

}
