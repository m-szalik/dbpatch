package org.jsoftware.dbpatch.command;

import org.apache.commons.io.IOUtils;
import org.hsqldb.jdbc.JDBCDriver;
import org.jsoftware.dbpatch.config.AbstractPatch;
import org.jsoftware.dbpatch.config.ConfigurationEntry;
import org.jsoftware.dbpatch.config.EnvSettings;
import org.jsoftware.dbpatch.config.Patch;
import org.jsoftware.dbpatch.config.PropertiesConfigurationParser;
import org.jsoftware.dbpatch.impl.AbstractDbManagerCredentialsCallback;
import org.jsoftware.dbpatch.impl.DbManager;
import org.jsoftware.dbpatch.impl.DbManagerCredentialsCallback;
import org.jsoftware.dbpatch.impl.DefaultPatchParser;
import org.jsoftware.dbpatch.impl.PatchParser;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDbCommandTest {
    private static final String JDBCURI="jdbc:hsqldb:mem:mymemdb";
    protected Connection connection;
    protected ConfigurationEntry configurationEntry;
    private File dir;
    private final static DbManagerCredentialsCallback DB_MANAGER_CREDENTIALS_CALLBACK = new AbstractDbManagerCredentialsCallback() {
        @Override
        protected String getPassword(SQLException lastSqlException, ConfigurationEntry configurationEntry) {
            return "";
        }
        @Override
        public String getUsername(ConfigurationEntry configurationEntry) throws SQLException {
            return "SA";
        }
    };
    private final static PatchParser PATCH_PARSER = new DefaultPatchParser();

    @Before
    public void setup() throws Exception {
        Class.forName(JDBCDriver.class.getName());
        connection = DriverManager.getConnection(JDBCURI, "SA", "");
        connection.setAutoCommit(true);
        File f = File.createTempFile(getClass().getSimpleName(), "dir");
        f.delete();
        f.mkdirs();
        dir = f;
        StringBuilder configuration = new StringBuilder();
        configuration.append("test.driverClass=").append(JDBCDriver.class.getName()).append("\n");
        configuration.append("test.jdbcUri=").append(JDBCURI).append("\n");
        configuration.append("test.username=SA\n");
        configuration.append("test.password=\n");
        configuration.append("test.patchDirs=").append(dir.getAbsolutePath()).append("/\n");
        PropertiesConfigurationParser configurationParser = new PropertiesConfigurationParser();
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(configuration.toString().getBytes());
            Collection<ConfigurationEntry> configurationEntries = configurationParser.parse(in);
            configurationEntry = configurationEntries.iterator().next();
            configurationEntry.validate();
        } finally {
            in.close();
        }
    }

    protected final <T extends AbstractSingleConfDbPatchCommand> T prepareCommand(Class<T> cmdClazz) throws Exception {
        Constructor<T> constructor = cmdClazz.getConstructor(EnvSettings.class);
        T instance = constructor.newInstance(EnvSettings.standalone());
        instance.setConf(configurationEntry);
        instance.setSelectedConfiguration("test");
        instance.setDirectory(dir);
        DbManager dbManager = new DbManager(configurationEntry);
        dbManager.init(DB_MANAGER_CREDENTIALS_CALLBACK);
        instance.setManager(dbManager);
        return instance;
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

    Patch addPatchToATestContext(String patchName, String fileName) throws IOException {
        File f = new File(dir, fileName);
        FileWriter fw = null;
        InputStream in = null;
        f.createNewFile();
        try {
            in = getClass().getResourceAsStream(fileName);
            if (in == null) {
                throw new IOException("Cannot find resource " + fileName);
            }
            fw = new FileWriter(f);
            IOUtils.copy(in, fw);
        } finally {
            IOUtils.closeQuietly(fw);
            IOUtils.closeQuietly(in);
        }
        Patch patch = new Patch();
        patch.setDbState(AbstractPatch.DbState.NOT_AVAILABLE);
        patch.setFile(f);
        patch.setName(patchName);
        PatchParser.ParseResult pr = PATCH_PARSER.parse(new FileInputStream(f), configurationEntry);
        patch.setStatementCount(pr.executableCount());
        return patch;
    }

    protected final Integer[] dbValues() throws SQLException {
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT ID FROM tab ORDER BY ID");
            List<Integer> result = new LinkedList<Integer>();
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result.toArray(new Integer[result.size()]);
        } finally {
            rs.close();
        }
    }

    public static String executeToString(ExecuteToStringCallback callback) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        callback.call(new PrintStream(out));
        out.close();
        return new String(out.toByteArray());
    }

    public interface ExecuteToStringCallback {
        void call(PrintStream printStream) throws Exception;
    }
}
