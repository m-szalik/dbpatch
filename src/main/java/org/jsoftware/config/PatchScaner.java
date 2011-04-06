package org.jsoftware.config;

import java.io.File;
import java.util.List;

import org.jsoftware.impl.DuplicatePatchNameException;

public interface PatchScaner {
	
	/**
	 * 
	 * @param directory base dir
	 * @param paths to scan (add directory if not absolute)
	 * @return
	 * @throws DuplicatePatchNameException 
	 */
	List<Patch> scan(File directory, String[] paths) throws DuplicatePatchNameException;

}
