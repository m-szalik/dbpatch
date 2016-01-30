package org.jsoftware.dbpatch.command;

import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.impl.CloseUtil;
import org.jsoftware.dbpatch.impl.DefaultPatchParser;
import org.jsoftware.dbpatch.impl.PatchParser.ParseResult;
import org.jsoftware.dbpatch.impl.PatchStatement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Command: Display parsed patch
 *
 * @author szalik
 */
public class HelpParseCommand extends AbstractCommand {

    public HelpParseCommand(EnvSettings envSettings) {
        super(envSettings);
    }


    public void execute() throws CommandExecutionException, CommandFailureException {
        String file = System.getProperty(envSettings.getDbPatchFile());
        if (file == null) {
            throw new CommandFailureException("Set system property \"" + envSettings.getDbPatchFile() + "\" to file you want to parse.");
        }
        File f = new File(file);
        if (!f.exists()) {
            throw new CommandFailureException("File " + f.getAbsolutePath() + " not found.");
        }
        DefaultPatchParser parser = new DefaultPatchParser();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            ParseResult pr = parser.parse(fis, null);
            log.info("Statements count: " + pr.executableCount());
            for (PatchStatement ps : pr.getStatements()) {
                log.info("{ " + ps + " }");
            }
        } catch (IOException e) {
            throw new CommandExecutionException("Exception", e);
        } finally {
            CloseUtil.close(fis);
        }
    }


}
