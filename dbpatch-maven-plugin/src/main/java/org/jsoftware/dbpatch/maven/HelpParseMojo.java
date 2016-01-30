package org.jsoftware.dbpatch.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.jsoftware.impl.CloseUtil;
import org.jsoftware.impl.DefaultPatchParser;
import org.jsoftware.impl.PatchParser.ParseResult;
import org.jsoftware.impl.PatchStatement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Display parsed patch
 *
 * @author szalik
 * @goal help-parse
 */
public class HelpParseMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        String file = System.getProperty("maven.dbpatch.file");
        if (file == null) {
            throw new MojoFailureException("Set system property \"maven.dbpatch.file\" to file you want to parse.");
        }
        File f = new File(file);
        if (!f.exists()) {
            throw new MojoFailureException("File " + f.getAbsolutePath() + " not found.");
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
            throw new MojoExecutionException("Exception", e);
        } finally {
            CloseUtil.close(fis);
        }
    }


}
