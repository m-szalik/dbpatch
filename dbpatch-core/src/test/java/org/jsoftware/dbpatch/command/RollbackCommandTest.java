package org.jsoftware.dbpatch.command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RollbackCommandTest extends AbstractDbCommandTest {
    private RollbackCommand command;

    @Before
    public void prepareDatabase() throws Exception {
        addPatchToATestContext("001.init", "001.init.sql");
        addPatchToATestContext("010.patch1", "010.patch1.sql");
        addPatchToATestContext("010.patch1", "010.patch1.sql.rollback");
        addPatchToATestContext("020.patch1", "020.patch2.sql");
        addPatchToATestContext("020.patch1", "020.patch2.sql.rollback");
        PatchCommand patchCommand = prepareCommand(PatchCommand.class);
        patchCommand.execute();
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, dbValues());
    }


    @Before
    public void createCommand() throws Exception {
        command = prepareCommand(RollbackCommand.class);
    }

    @Test
    public void testRollbackCommandSingle() throws Exception {
        command.setRollbackCommandConf(new RollbackCommandConf(RollbackCommandConf.Action.SINGLE, "010.patch1"));
        command.execute();
        Assert.assertArrayEquals(new Integer[]{3}, dbValues());
    }

    @Test
    public void testRollbackCommandStopOn() throws Exception {
        command.setRollbackCommandConf(new RollbackCommandConf(RollbackCommandConf.Action.STOP_ON, "010.patch1"));
        command.execute();
        Assert.assertArrayEquals(new Integer[]{ }, dbValues());
    }

}