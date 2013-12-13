package org.jsoftware.config;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

/**
 * @author szalik
 */
public class ConfigurationEntryTest {
    private ConfigurationEntry ce;

    @Before
    public void setUp() throws Exception {
        ce = new ConfigurationEntry();
        ce.setJdbcUri("test");
        ce.setDriverClass("driver");
    }

    @Test
    public void testAutodetectRollbackDirs1() throws ParseException {
        ce.setPatchDirs("/abc/,/xyz");
        ce.validate();
        org.junit.Assert.assertEquals("/abc/*.rollback,/xyz/*.rollback", ce.getRollbackDirs());
    }

    @Test
    public void testAutodetectRollbackDirs2() throws ParseException {
        ce.setPatchDirs("/abc/*.sql");
        ce.validate();
        org.junit.Assert.assertEquals("/abc/*.rollback", ce.getRollbackDirs());
    }

    @Test
    public void testAutodetectRollbackDirsChangedDefault() throws ParseException {
        ce.setPatchDirs("/abc/");
        ce.setRollbackSuffix("*.undo");
        ce.validate();
        org.junit.Assert.assertEquals("/abc/*.undo", ce.getRollbackDirs());
    }
}
