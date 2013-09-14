package org.jsoftware.impl.extension;

import org.jsoftware.config.Patch;
import org.jsoftware.config.dialect.PatchExecutionResult;
import org.jsoftware.impl.PatchStatement;

import java.sql.Connection;
import java.sql.SQLException;

public interface Extension {

	/** Before patching procedure starts */
	void beforePatching(Connection connection);
	
	/** After patching process ends */
	void afterPatching(Connection connection);
	
	/** Before the patch is executed */
	void beforePatch(Connection connection, Patch patch);
	
	/** After the patch is executed
	 * @throws SQLException 
	 **/
	void afterPatch(Connection connection, Patch patch, Exception ex) throws SQLException;
	
	/** Before patch's statement is executed */
	void beforePatchStatement(Connection connection, Patch patch, PatchStatement statement);
	
	/** After patch's statement is executed */
	void afterPatchStatement(Connection connection, Patch patch, PatchExecutionResult result);
	
}

