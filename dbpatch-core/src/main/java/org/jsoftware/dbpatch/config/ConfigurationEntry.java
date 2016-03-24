package org.jsoftware.dbpatch.config;

import org.jsoftware.dbpatch.config.dialect.DefaultDialect;
import org.jsoftware.dbpatch.config.dialect.Dialect;
import org.jsoftware.dbpatch.config.dialect.DialectFinder;
import org.jsoftware.dbpatch.impl.DefaultPatchParser;
import org.jsoftware.dbpatch.impl.DirectoryPatchScanner;
import org.jsoftware.dbpatch.impl.PatchParser;
import org.jsoftware.dbpatch.impl.extension.Extension;
import org.jsoftware.dbpatch.impl.extension.TkExtensionAndStrategy;
import org.jsoftware.dbpatch.log.LogFactory;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;

/**
 * Represents configuration for single database profile
 */
public class ConfigurationEntry implements Serializable {
    private static final Map<String, ApplyStrategy> applyStrategies;
    private static final Map<String, Extension> availableExtensions;

    static {
        applyStrategies = new HashMap<String, ApplyStrategy>();
        applyStrategies.put("missing", new MissingApplyStrategy());
        applyStrategies.put("last", new LastApplyStrategy());
        applyStrategies.put("tk", new TkExtensionAndStrategy());

        availableExtensions = new HashMap<String, Extension>();
        availableExtensions.put("tk", new TkExtensionAndStrategy());
    }

    private static final long serialVersionUID = 1L;
    private final String id;
    private final File baseDir;
    private String jdbcUri;
    private String driverClass;
    private String user;
    private String password;
    private Dialect dialect;
    private String patchDirs;
    private String rollbackDirs;
    private PatchScanner patchScanner;
    private final PatchParser patchParser;
    private ApplyStrategy applyStarters;
    private Collection<Extension> extensions;
    private String patchEncoding;
    private String rollbackSuffix = "*.rollback";


    protected ConfigurationEntry(String id, File baseDir) {
        this.id = id;
        this.baseDir = baseDir;
        patchScanner = new DirectoryPatchScanner();
        patchParser = new DefaultPatchParser();
        applyStarters = new MissingApplyStrategy();
        extensions = Collections.emptySet();
        patchEncoding = Charset.defaultCharset().name();
    }

    public Charset getPatchEncoding() {
        return Charset.forName(patchEncoding);
    }

    public void setPatchEncoding(String patchEncoding) {
        this.patchEncoding = Charset.forName(patchEncoding).name();
    }

    public void setEncoding(String patchEncoding) {
        setPatchEncoding(patchEncoding);
    }

    public ApplyStrategy getApplyStarters() {
        return applyStarters;
    }

    public void setApplyStarters(String applyStarters) {
        applyStarters = applyStarters.toLowerCase().trim();
        this.applyStarters = applyStrategies.get(applyStarters);
        if (this.applyStarters == null) {
            throw new IllegalArgumentException("Can not find applyStrategy for \"" + applyStarters + "\"");
        }
    }

    public void setStrategy(String strategy) {
        setApplyStarters(strategy);
    }

    public Collection<Extension> getExtensions() {
        return Collections.unmodifiableCollection(extensions);
    }

    public String getRollbackSuffix() {
        return rollbackSuffix;
    }

    public void setRollbackSuffix(String rollbackSuffix) {
        this.rollbackSuffix = rollbackSuffix;
    }

    public void setExtensions(String extensions) {
        HashSet<Extension> exs = new HashSet<Extension>();
        for (String ext : extensions.split(",")) {
            ext = ext.trim();
            if (ext.length() == 0) {
                continue;
            }
            Extension extension = availableExtensions.get(ext);
            if (extension == null) {
                throw new IllegalArgumentException("Can not find extension for \"" + ext + "\"");
            }
            exs.add(extension);
        }
        this.extensions = exs;
    }

    public String getId() {
        return id;
    }

    public String getJdbcUri() {
        return jdbcUri;
    }

    public void setJdbcUri(String jdbcUri) {
        this.jdbcUri = jdbcUri;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUsername(String username) {
        setUser(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialectInstance(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setDialect(String dialect) {
        Dialect dialect2 = DialectFinder.find(dialect);
        setDialectInstance(dialect2);
    }

    public void setPatchDirs(String patchDir) {
        this.patchDirs = patchDir;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public PatchParser getPatchParser() {
        return patchParser;
    }

    public PatchScanner getPatchScanner() {
        return patchScanner;
    }

    public void setPatchScanner(PatchScanner patchScanner) {
        this.patchScanner = patchScanner;
    }

    public String getRollbackDirs() {
        return rollbackDirs;
    }

    public void setRollbackDirs(String rollbackDirs) {
        this.rollbackDirs = rollbackDirs;
    }

    public void validate() throws ParseException {
        checkNull(jdbcUri, "jdbcUri");
        checkNull(driverClass, "driverClass");
        checkNull(patchDirs, "patchDirs");
        if (rollbackDirs == null) {
            StringBuilder sbr = new StringBuilder();
            for (String dir : patchDirs.split("[,]")) {
                int x = dir.indexOf('*');
                if (x >= 0) {
                    dir = dir.substring(0, x - 1);
                }
                sbr.append(dir);
                if (!dir.endsWith(File.separator)) {
                    sbr.append(File.separator);
                }
                sbr.append(rollbackSuffix).append(',');
            }
            rollbackDirs = sbr.substring(0, sbr.length() - 1);
        }
        if (dialect == null) {
            dialect = DialectFinder.findByDriverClass(driverClass);
            if (dialect == null) {
                LogFactory.getInstance().info("Cannot detect dialect using default.");
                dialect = new DefaultDialect();
            }
        }
    }

    private void checkNull(Object what, String key) throws ParseException {
        if (what == null) {
            throw new ParseException("Property " + key + " in not set for configuration - " + id, 0);
        }
    }

    public String getPatchDirs() {
        return patchDirs;
    }


    public String toString() {
        ToStringBuilder tsb = new ToStringBuilder();
        tsb.add("id", id);
        tsb.add("driverClass", driverClass).add("jdbcUri", jdbcUri);
        tsb.add("user", user).add("password", "****");
        tsb.add("patchDirs", patchDirs);
        tsb.add("rollbackDirs", rollbackDirs);
        tsb.add("encoding", patchEncoding);
        tsb.add("strategy", applyStarters.getClass().getSimpleName());
        return tsb.toString();
    }

    public boolean isInteractivePasswordAllowed() {
        return true; // TODO move it into configuration ????
    }

    /**
     * @return base dir for patches - a dir of config file or project pom.xml or build.gradle
     */
    public File getBaseDir() {
        return baseDir;
    }
}


class ToStringBuilder {
    private final StringBuilder sb = new StringBuilder();

    public ToStringBuilder add(String name, Object value) {
        sb.append(name).append('=').append(value == null ? "-" : value).append('\n');
        return this;
    }


    public String toString() {
        return sb.toString();
    }

}