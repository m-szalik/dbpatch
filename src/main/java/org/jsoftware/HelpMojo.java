package org.jsoftware;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jsoftware.impl.CloseUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/**
 * Display help
 * @goal help
 * @author szalik
 */
public class HelpMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		InputStream in = getClass().getResourceAsStream("/dbpatch-help.txt");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String s;
			while((s = br.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} finally {
			CloseUtil.close(br);
			CloseUtil.close(in);
		}
	}
	
}
