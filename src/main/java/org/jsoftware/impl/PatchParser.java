package org.jsoftware.impl;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.ConfigurationEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public interface PatchParser {
    public interface ParseResult {
        List<PatchStatement> getStatements();

        int executableCount();

        int totalCount();
    }

    ParseResult parse(InputStream inputStream, ConfigurationEntry ce) throws IOException;

    ParseResult parse(AbstractPatch p, ConfigurationEntry ce) throws IOException;
}
