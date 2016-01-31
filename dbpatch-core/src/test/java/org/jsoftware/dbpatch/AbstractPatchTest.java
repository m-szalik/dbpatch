package org.jsoftware.dbpatch;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.junit.Test;

/**
 * @author szalik
 */
public class AbstractPatchTest {

    @Test
    public void testNormalizeName1() throws Exception {
        org.junit.Assert.assertEquals("102.patch-1", AbstractPatch.normalizeName("102.patch-1"));
    }

    @Test
    public void testNormalizeName2() throws Exception {
        org.junit.Assert.assertEquals("102.patch-1", AbstractPatch.normalizeName("102.patch-1.sql"));
    }

    @Test
    public void testNormalizeName3() throws Exception {
        org.junit.Assert.assertEquals("102.patch-1", AbstractPatch.normalizeName("102.patch-1.rollback.sql"));
    }
}
