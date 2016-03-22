package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.Patch;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ListCommandTest extends AbstractDbTest {

    @Test
    public void testListCommand() throws Exception {
        ListCommand command = createAndSetupCommand(ListCommand.class);
        List<Patch> list;
        list = command.getList();
        Assert.assertEquals(0, list.size());
        addPatch("001.patch1.sql", "INSERT INTO tab (a) VALUES (1);\n");
        list = command.getList();
        Assert.assertEquals(1, list.size());
    }


}