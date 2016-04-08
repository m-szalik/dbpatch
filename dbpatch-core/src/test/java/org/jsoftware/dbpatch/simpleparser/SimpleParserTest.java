package org.jsoftware.dbpatch.simpleparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;


public class SimpleParserTest {
    private String input;

    @Before
    public void setUp() throws IOException {
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
        final AtomicInteger tokenCount = new AtomicInteger(0);
        SimpleParser simpleParser = new SimpleParser("--", "//", ";", "\n");
        simpleParser.parse(input, new SimpleParserCallback() {
            public void tokenFound(SimpleParserCallbackContext ctx, String token) {
                System.out.println("Token " + ("\n".equals(token) ? "NL" : token) + " - " + ctx.getTextBefore() + " :" + ctx);
                tokenCount.incrementAndGet();
            }
            public void documentStarts() {
                // nothing to do here
            }
            public void documentEnds(SimpleParserCallbackContext ctx) {
                System.out.println("End - " + ctx.getTextBefore());
            }
        });
        Assert.assertEquals(20, tokenCount.get());
    }
}
