package org.jsoftware.command;

import org.jsoftware.impl.CloseUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Command: Display help
 *
 * @author szalik
 */
public class HelpCommand extends AbstractCommand {

    public void execute() throws CommandExecutionException {
        InputStream in = getClass().getResourceAsStream("/dbpatch-help.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            throw new CommandExecutionException(e.getMessage(), e);
        } finally {
            CloseUtil.close(br);
            CloseUtil.close(in);
        }
    }

}
