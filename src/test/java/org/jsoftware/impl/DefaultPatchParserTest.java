package org.jsoftware.impl;

import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class DefaultPatchParserTest {

    private static int countStatements(List<PatchStatement> list, boolean executable) {
        int c = 0;
        for (PatchStatement ps : list) {
            if (ps.isExecutable() == executable) {
                c++;
            }
        }
        return c;
    }

    @Test
    public void parse() throws IOException {
        DefaultPatchParser parser = new DefaultPatchParser();
        List<PatchStatement> list = parser.parse(getClass().getResourceAsStream("input.txt"), null).getStatements();
        Assert.assertEquals(6, countStatements(list, true));
        Assert.assertEquals(4, countStatements(list, false));
    }


    @Test
    public void parseBlock() throws IOException {
        DefaultPatchParser parser = new DefaultPatchParser();
        List<PatchStatement> list = parser.parse(getClass().getResourceAsStream("input-block.txt"), null).getStatements();
//		for(PatchStatement ps : list) {
//			System.out.println(ps.isExecutable() + "  " + ps.toString());
//		}
        Assert.assertEquals(4, countStatements(list, true));
        Assert.assertEquals(3, countStatements(list, false));
    }

    @Test
    public void parseSemicon() throws IOException {
        DefaultPatchParser parser = new DefaultPatchParser();
        ByteArrayInputStream bais = new ByteArrayInputStream("INSERT INTO a (\"ab;c\\\"d\", 'za;p\\'o')".getBytes());
        List<PatchStatement> list = parser.parse(bais, null).getStatements();
        Assert.assertEquals(1, countStatements(list, true));
        Assert.assertEquals("INSERT INTO a (\"ab;c\\\"d\", 'za;p\\'o')", list.get(0).getCode());
    }

    @Test
    public void parsesWithDelimiter() throws IOException {
        DefaultPatchParser parser = new DefaultPatchParser("\n");
        List<PatchStatement> list = parser.parse(getClass().getResourceAsStream("custom-delimiter.txt"), null).getStatements();
        Assert.assertEquals(7, countStatements(list, true));
        Assert.assertEquals(1, countStatements(list, false));
    }

    @Test
    public void comments() throws IOException {
        DefaultPatchParser parser = new DefaultPatchParser();
        List<PatchStatement> list = parser.parse(getClass().getResourceAsStream("input-comments.txt"), null).getStatements();
        Assert.assertEquals(4, countStatements(list, true));
        Assert.assertEquals(3, countStatements(list, false));
    }
}
