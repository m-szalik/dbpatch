package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.Patch;
import org.jsoftware.dbpatch.config.RollbackPatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class RollbackListCommandTest extends AbstractDbCommandTest {

    @Test
    public void testRollbackListCommand() throws Exception {
        RollbackListCommand command = prepareCommand(RollbackListCommand.class);
        List<Patch> patchList = new LinkedList<Patch>();
        List<RollbackPatch> rollbackPatchList;

        rollbackPatchList = command.generateList(patchList);
        Assert.assertEquals(0, rollbackPatchList.size());

        Patch patch = addPatchToATestContext("010.patch1", "010.patch1.sql");
        patchList.add(patch);
        rollbackPatchList = command.generateList(patchList);
        Assert.assertEquals(1, rollbackPatchList.size());
        Assert.assertTrue(rollbackPatchList.get(0).isMissing());

        addPatchToATestContext("010.patch1", "010.patch1.sql.rollback");
        rollbackPatchList = command.generateList(patchList);
        RollbackPatch rollbackPatch = rollbackPatchList.get(0);
        Assert.assertFalse(rollbackPatch.isMissing());
        Assert.assertFalse(rollbackPatch.canApply());
    }
}