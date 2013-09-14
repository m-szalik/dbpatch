package org.jsoftware;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.InteractivePanel;
import org.jsoftware.log.LogFactory;
import org.jsoftware.maven.AbstractDbPatchMojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

public class DbPatch {

	public static void main(String[] args) throws ParseException, IOException, MojoFailureException, MojoExecutionException {
		LogFactory.initLocal();
        File confFile;
        if (args.length > 1) {
           confFile = new File(args[1]);
           if (! confFile.exists()) {
               throw new FileNotFoundException("Cannot find configuration file - " + confFile.getAbsolutePath());
           }
        } else {
            confFile = null;
        }
        if (args.length == 0) { // interactive mode
            LogFactory.getInstance().debug("Lunching interactive mode.");
            Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(confFile);
            InteractivePanel interactive = new InteractivePanel(conf);
            interactive.start();
        } else { // mojo (text) mode
            AbstractMojo mojo = argToMojo(args[1]);
            LogFactory.getInstance().debug("Lunching text mode - " + mojo + ".");
            if (mojo instanceof AbstractDbPatchMojo) {
                ((AbstractDbPatchMojo) mojo).setConfigFile(confFile);
            }
            if (mojo == null) {
                System.err.println("Arg 1 must be mojo name, arg 2 can be configuration file.");
                new HelpMojo().execute();
            } else {
                mojo.execute();
            }
        }
	}

    private static AbstractMojo argToMojo(String mojoArg) {
        if ("help".equalsIgnoreCase(mojoArg)) {
            return new HelpMojo();
        }
        if ("help-parse".equalsIgnoreCase(mojoArg)) {
            return new HelpParseMojo();
        }
        if ("interactive".equalsIgnoreCase(mojoArg)) {
            return new InteractiveMojo();
        }
        if ("list".equalsIgnoreCase(mojoArg)) {
            return new ListMojo();
        }
        if ("patch".equalsIgnoreCase(mojoArg)) {
            return new PatchMojo();
        }
        if ("skip-errors".equalsIgnoreCase(mojoArg)) {
            return new SkipErrorsMojo();
        }
        return null;
    }

}
