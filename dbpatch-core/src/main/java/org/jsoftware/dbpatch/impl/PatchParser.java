package org.jsoftware.dbpatch.impl;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.ConfigurationEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Patch parser.
 * <p>Splits Patches and RollbackPatches to PatchStatements</p>
 */
public interface PatchParser {
    interface ParseResult {
        List<PatchStatement> getStatements();

        int executableCount();

        int totalCount();
    }

    ParseResult parse(InputStream inputStream, ConfigurationEntry ce) throws IOException;

    ParseResult parse(AbstractPatch p, ConfigurationEntry ce) throws IOException;
}
