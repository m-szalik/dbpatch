package org.jsoftware.dbpatch.command;

import org.hsqldb.jdbc.JDBCDriver;
import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.config.PropertiesConfigurationParser;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

class AbstractDbTest {
    private static final String JDBCURI="jdbc:hsqldb:mem:mymemdb";
    protected Connection connection;
    private File dir;

    @Before
    public void setup() throws SQLException, ClassNotFoundException, IOException {
        Class.forName(JDBCDriver.class.getName());
        connection = DriverManager.getConnection(JDBCURI, "SA", "");
        connection.setAutoCommit(true);
        File f = File.createTempFile(getClass().getSimpleName(), "dir");
        f.delete();
        f.mkdirs();
        dir = f;
    }

    @After
    public void tearDown() throws Exception {
        connection.createStatement().execute("SHUTDOWN");
        connection.close();
        File[] files = dir.listFiles();
        if (files != null) {
            for(File f : files) {
                f.delete();
            }
        }
        dir.delete();
    }

    <T extends AbstractSingleConfDbPatchCommand> T createAndSetupCommand(Class<T> cmdClazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ParseException {
        StringBuilder configuration = new StringBuilder();
        configuration.append("test.driverClass=").append(JDBCDriver.class.getName()).append("\n");
        configuration.append("test.jdbcUri=").append(JDBCURI).append("\n");
        configuration.append("test.username=SA\n");
        configuration.append("test.password=\n");
        configuration.append("test.patchDirs=").append(dir.getAbsolutePath()).append("/\n");
        Constructor<T> constructor = cmdClazz.getConstructor(EnvSettings.class);
        T instance = constructor.newInstance(EnvSettings.standalone());
        PropertiesConfigurationParser configurationParser = new PropertiesConfigurationParser();
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(configuration.toString().getBytes());
            Collection<ConfigurationEntry> configurationEntries = configurationParser.parse(in);
            instance.setConf(configurationEntries.iterator().next());
            instance.setSelectedConfiguration("test");
            instance.setDirectory(dir);
            return instance;
        } finally {
            in.close();
        }
    }

    void addPatch(String fileName, String content) throws IOException {
        File f = new File(dir, fileName);
        FileWriter fw = null;
        f.createNewFile();
        try {
            fw = new FileWriter(f);
            fw.append(content).append('\n');
        } finally {
            if (fw != null) {
                fw.close();
            }
        }
    }

}
