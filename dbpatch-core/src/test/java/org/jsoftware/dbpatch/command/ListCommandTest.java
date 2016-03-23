package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.Patch;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ListCommandTest extends AbstractDbCommandTest {


    @Test
    public void testListCommand() throws Exception {
        ListCommand command = prepareCommand(ListCommand.class);
        List<Patch> list;
        list = command.getList();
        Assert.assertEquals(0, list.size());
        addPatchToATestContext("010.patch1", "010.patch1.sql");
        list = command.getList();
        Assert.assertEquals(1, list.size());

        Patch patch = list.get(0);
        Assert.assertEquals("010.patch1", patch.getName());
        Assert.assertEquals(2, patch.getStatementCount());
        Assert.assertEquals(AbstractPatch.DbState.NOT_AVAILABLE, patch.getDbState());
        Assert.assertNull(patch.getDbDate());
    }
}