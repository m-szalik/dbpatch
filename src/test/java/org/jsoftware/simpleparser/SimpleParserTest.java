package org.jsoftware.simpleparser;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SimpleParserTest {

    private String input;

    @Before
    public void setup() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("input.txt")));
        StringBuilder sb = new StringBuilder();
        String l;
        while ((l = br.readLine()) != null) {
            sb.append(l).append('\n');
        }
        br.close();
        input = sb.toString();
    }

    @Test
    public void parseTest1() {
        SimpleParser simpleParser = new SimpleParser("--", "//", ";", "\n");
        simpleParser.parse(input, new SimpleParserCallback() {
            public void tokenFound(SimpleParserCallbackContext ctx, String token) {
                System.out.println("Token " + (token.equals("\n") ? "NL" : token) + " - " + ctx.getTextBefore() + " :" + ctx);
            }

            public void documentStarts() {
            }

            public void documentEnds(SimpleParserCallbackContext ctx) {
                System.out.println("End - " + ctx.getTextBefore());
            }
        });
    }
}
