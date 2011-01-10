package org.jsoftware.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jsoftware.config.Patch;


public interface PatchParser {
	public interface ParseResult {
		List<PatchStatement> getStatements();
		int executableCount();
		int totalCount();
	}

	ParseResult parse(InputStream inputStream) throws IOException;

	ParseResult parse(Patch p) throws IOException;
}
