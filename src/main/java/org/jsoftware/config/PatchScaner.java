package org.jsoftware.config;

import java.io.File;
import java.util.List;

public interface PatchScaner {
	
	/**
	 * 
	 * @param directory base dir
	 * @param paths to scan (add directory if not absolute)
	 * @return
	 */
	List<Patch> scan(File directory, String[] paths);

}
