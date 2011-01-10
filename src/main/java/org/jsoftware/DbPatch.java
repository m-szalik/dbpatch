package org.jsoftware;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

import org.jsoftware.config.AbstractConfigurationParser;
import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.impl.InteractivePanel;
import org.jsoftware.log.LogFactory;

public class DbPatch {

	public static void main(String[] args) throws ParseException, IOException {
		LogFactory.initLocal();
		if (args.length == 0) {
			System.err.println("Argument must be a configuration file.");
			System.exit(1);
		}
		Collection<ConfigurationEntry> conf = AbstractConfigurationParser.discoverConfiguration(new File(args[0]));
		InteractivePanel interactive = new InteractivePanel(conf);
		interactive.start();
	}
	
}
