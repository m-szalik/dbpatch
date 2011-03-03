package org.jsoftware.impl.extension;

import java.sql.Connection;
import java.sql.SQLException;

import org.jsoftware.config.Patch;
import org.jsoftware.config.dialect.PatchExecutionResult;
import org.jsoftware.impl.PatchStatement;

public interface Extension {

	/** Przed rozpoczeciem procesu patchowania */
	void beforePatching(Connection connection);
	
	/** Po zakończenu procesu patchowania */
	void afterPatching(Connection connection);
	
	/** Przed konkretnym patchem */
	void beforePatch(Connection connection, Patch patch);
	
	/** Po konkretnym patchu 
	 * @throws SQLException 
	 **/
	void afterPatch(Connection connection, Patch patch, Exception ex) throws SQLException;
	
	/** Przed wykonaniem patch statementu */
	void beforePatchStatement(Connection connection, Patch patch, PatchStatement statement);
	
	/** Po wykonaniu patch statementu */
	void afterPatchStatement(Connection connection, Patch patch, PatchExecutionResult result);
	
}

