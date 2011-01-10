package org.jsoftware.impl.extension;

import java.sql.Connection;
import java.sql.SQLException;

import org.jsoftware.config.Patch;
import org.jsoftware.impl.PatchStatement;

public interface Extension {

	void beforePatching(Connection connection);
	
	void afterPatching(Connection connection);
	
	void beforePatch(Connection connection, Patch patch);
	
	void afterPatchComplete(Connection connection, Patch patch) throws SQLException;
	
	void afterPatchError(Connection connection, Patch patch, Exception ex);
	
	void beforePatchStatement(Connection connection, Patch patch, PatchStatement statement);
	
	void afterPatchStatement(Connection connection, Patch patch, PatchStatement statement);
	
}

