package org.jsoftware.dbpatch.impl;

import org.jsoftware.dbpatch.config.Patch;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author szalik
 */
public class SimplePatchScannerTest {
    private final SimplePatchScanner simplePatchScanner = new SimplePatchScanner() {
        protected void sortDirectory(List<Patch> dirPatchList) {
        }
        protected void sortAll(List<Patch> allPatchList) {
        }
    };

    @Test
    public void testParsePatchDirs() throws Exception {
        File dir = new File(".").getCanonicalFile();
        List<DirMask> result = simplePatchScanner.parsePatchDirs(dir, new String[]{"*.txt"});
        DirMask dm = result.get(0);
        assertEquals(dir, dm.getDir());
        assertEquals("*.txt", dm.getMask());
    }

    @Test
    public void testScan() throws Exception {
        File dir = new File(".").getCanonicalFile();
        if (dir.getAbsolutePath().endsWith("dbpatch-core")) {
            dir = dir.getParentFile();
        }
        List<Patch> result = simplePatchScanner.scan(dir, new String[]{"*.md"});
        File f = result.get(0).getFile();
        assertTrue(f.getAbsolutePath().endsWith(".md"));
    }
}
