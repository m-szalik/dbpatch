package org.jsoftware.dbpatch.command;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.PrintStream;

import static org.jsoftware.dbpatch.command.AbstractDbCommandTest.executeToString;

public class HelpCommandTest {

    static class ExecuteToStringCallbackHelpCmd implements AbstractDbCommandTest.ExecuteToStringCallback {
        private final HelpCommand helpCommand;
        ExecuteToStringCallbackHelpCmd(HelpCommand helpCommand) {this.helpCommand = helpCommand;}
        @Override
        public void call(PrintStream printStream) throws Exception {
           helpCommand.execute(printStream);
        }
    }

    @Test
    public void testStandalone() throws Exception {
        HelpCommand helpCommand = HelpCommand.helpCommandStandalone();
        String out = executeToString(new ExecuteToStringCallbackHelpCmd(helpCommand));
        Assert.assertThat(out, CoreMatchers.containsString("Commands"));
        Assert.assertThat(out, CoreMatchers.containsString("[dbpatch.file]"));
    }

    @Test
    public void testMaven() throws Exception {
        HelpCommand helpCommand = HelpCommand.helpCommandMaven();
        String out = executeToString(new ExecuteToStringCallbackHelpCmd(helpCommand));
        Assert.assertThat(out, CoreMatchers.containsString("Goals"));
        Assert.assertThat(out, CoreMatchers.containsString("[maven.dbpatch.file]"));
    }

    @Test
    public void testGradle() throws Exception {
        HelpCommand helpCommand = HelpCommand.helpCommandGradle();
        String out = executeToString(new ExecuteToStringCallbackHelpCmd(helpCommand));
        Assert.assertThat(out, CoreMatchers.containsString("Tasks"));
        Assert.assertThat(out, CoreMatchers.containsString("[gradle.dbpatch.file]"));
    }

}
