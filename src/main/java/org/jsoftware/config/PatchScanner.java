package org.jsoftware.config;

import org.jsoftware.impl.DuplicatePatchNameException;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public interface PatchScanner extends Serializable {
	
	/**
	 * 
	 * @param directory base dir
	 * @param paths to scan (add directory if not absolute)
	 * @return
	 * @throws DuplicatePatchNameException 
	 */
	List<Patch> scan(File directory, String[] paths) throws DuplicatePatchNameException;

    /**
     * @param directory base dir
     * @param paths to scan (add directory if not absolute)
     * @return null if no file was found
     */
    File findRollbackFile(File directory, String[] paths, Patch patch) throws DuplicatePatchNameException;
}
