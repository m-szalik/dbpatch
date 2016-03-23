package org.jsoftware.dbpatch.command;

import org.apache.commons.io.output.NullOutputStream;
import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.config.Patch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.PrintStream;

public class HelpParseCommandTest extends AbstractDbCommandTest {
    private HelpParseCommand command;

    @Before
    public void setUp() throws Exception {
        command = new HelpParseCommand(EnvSettings.standalone());
    }

    @Test
    public void testParse() throws Exception {
        Patch patch = addPatchToATestContext("010.patch1", "010.patch1.sql");
        final File patchFile = patch.getFile();
        String out = executeToString(new ExecuteToStringCallback() {
            @Override
            public void call(PrintStream printStream) throws Exception {
                command.execute(printStream, patchFile);
            }
        });
        Assert.assertEquals("Statements count: 2\n" +
                "{SqlPatchStatement:INSERT INTO tab (ID) VALUES (1)}\n" +
                "{SqlPatchStatement:INSERT INTO tab (ID) VALUES (2)}", out.trim());
    }

    @Test(expected = CommandFailureException.class)
    public void testParseFileNotFount() throws Exception {
        command.execute(new PrintStream(new NullOutputStream()), new File("not.existing.file.sql"));
    }
}