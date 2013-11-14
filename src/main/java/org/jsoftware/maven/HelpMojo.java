package org.jsoftware.maven;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.command.HelpCommand;
import org.jsoftware.impl.CloseUtil;
import org.jsoftware.maven.CommandMojoAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/**
 * Display help
 * @goal help
 * @author szalik
 */
public class HelpMojo extends CommandMojoAdapter<HelpCommand> {

    protected HelpMojo(HelpCommand command) {
        super(command);
    }
}
