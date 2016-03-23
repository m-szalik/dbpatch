package org.jsoftware.dbpatch.command;

import org.junit.Assert;
import org.junit.Test;

public class PatchCommandTest extends AbstractDbCommandTest {

    @Test
    public void testPatchAndRollbackCommand() throws Exception {
        PatchCommand command = prepareCommand(PatchCommand.class);
        addPatchToATestContext("001.init", "001.init.sql");
        addPatchToATestContext("010.patch1", "010.patch1.sql");
        command.execute();
        Assert.assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(2)}, dbValues());
    }

}