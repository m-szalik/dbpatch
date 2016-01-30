package org.jsoftware.dbpatch.config;

import org.junit.Test;

import java.util.Properties;

public class PropertiesConfigurationParserTest {

    @Test
    public void testEvaluatePropertyValuesInternal1() throws Exception {
        Properties p = new Properties();
        p.setProperty("a", "A");
        p.setProperty("b", "1${a}2");
        PropertiesConfigurationParser.evaluatePropertyValuesInternal(p);
        org.junit.Assert.assertEquals("1A2", p.getProperty("b"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testEvaluatePropertyValuesInternalIllegalArgument() throws Exception {
        Properties p = new Properties();
        p.setProperty("b", "1${a384z}2");
        PropertiesConfigurationParser.evaluatePropertyValuesInternal(p);
    }

}
