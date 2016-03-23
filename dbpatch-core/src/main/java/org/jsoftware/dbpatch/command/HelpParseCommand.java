package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.impl.CloseUtil;
import org.jsoftware.dbpatch.impl.DefaultPatchParser;
import org.jsoftware.dbpatch.impl.PatchParser.ParseResult;
import org.jsoftware.dbpatch.impl.PatchStatement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;


/**
 * Command: Display parsed patch
 *
 * @author szalik
 */
public class HelpParseCommand extends AbstractCommand {

    public HelpParseCommand(EnvSettings envSettings) {
        super(envSettings);
    }

    @Override
    public void execute() throws CommandExecutionException, CommandFailureException {
        String file = System.getProperty(envSettings.getDbPatchFile());
        if (file == null) {
            throw new CommandFailureException("Set system property \"" + envSettings.getDbPatchFile() + "\" to file you want to parse.");
        }
        execute(System.out, new File(file));
    }

    void execute(PrintStream out, File patchFile) throws CommandExecutionException, CommandFailureException {
        if (!patchFile.exists()) {
            CommandFailureException cfe = new CommandFailureException("File " + patchFile.getAbsolutePath() + " not found.");
            cfe.initCause(new FileNotFoundException(patchFile.getAbsolutePath()));
            throw cfe;
        }
        DefaultPatchParser parser = new DefaultPatchParser();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(patchFile);
            ParseResult pr = parser.parse(fis, null);
            out.append("Statements count: ").append(Integer.toString(pr.executableCount())).append('\n');
            for (PatchStatement ps : pr.getStatements()) {
                out.append('{').append(ps.toString()).append("}\n");
            }
        } catch (IOException e) {
            throw new CommandExecutionException("Exception", e);
        } finally {
            CloseUtil.close(fis);
        }
    }


}
