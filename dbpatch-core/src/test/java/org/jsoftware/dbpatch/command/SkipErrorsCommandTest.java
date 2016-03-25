package org.jsoftware.dbpatch.command;

import org.junit.Assert;
import org.junit.Test;

public class SkipErrorsCommandTest extends AbstractDbCommandTest {

    @Test
    public void testSkipErrorsCommand() throws Exception {
        PatchCommand patchCommand = prepareCommand(PatchCommand.class);
        addPatchToATestContext("001.init", "001.init.sql");
        addPatchToATestContext("010.patch1", "010.patch1.sql");
        addPatchToATestContext("015.patchWithErrors", "015.patchWithErrors.sql");
        addPatchToATestContext("020.patch1", "020.patch2.sql");

        try {
            patchCommand.execute();
            Assert.fail("No exception");
        } catch (CommandExecutionException ex) {
            Assert.assertArrayEquals(new Integer[]{1, 2}, dbValues());
        }

        SkipErrorsCommand skipErrorsCommand = prepareCommand(SkipErrorsCommand.class);
        skipErrorsCommand.execute();

        // patch again
        patchCommand.execute();
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, dbValues());
    }

}