package org.jsoftware.config;

import java.sql.Connection;
import java.util.List;

public interface ApplyStrategy {
	
	List<Patch> filter(Connection connection, List<Patch> patches);
	
}
