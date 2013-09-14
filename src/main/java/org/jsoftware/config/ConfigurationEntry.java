package org.jsoftware.config;

import org.jsoftware.config.dialect.DefaultDialect;
import org.jsoftware.config.dialect.Dialect;
import org.jsoftware.config.dialect.DialectFinder;
import org.jsoftware.impl.DefaultPatchParser;
import org.jsoftware.impl.DirectoryPatchScanner;
import org.jsoftware.impl.PatchParser;
import org.jsoftware.impl.extension.Extension;
import org.jsoftware.impl.extension.TkExtensionAndStrategy;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;

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
	private String jdbcUri;
	private String driverClass;
	private String user;
	private String password;
	private Dialect dialect;
	private String patchDirs;
	private PatchScanner patchScanner;
	private final PatchParser patchParser;
	private ApplyStrategy applyStarters;
	private Collection<Extension> extensions;
	private String patchEncoding;
	
	
	public ConfigurationEntry() {
		this("maven:pom.xml");
	}
	
	ConfigurationEntry(String id) {
		this.id = id;
		dialect = new DefaultDialect();
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
	
	public void setExtensions(String extensions) {
		HashSet<Extension> exs = new HashSet<Extension>();
		for(String ext : extensions.split(",")) {
			ext = ext.trim();
			if (ext.length() == 0) continue;
			Extension extension = availableExtensions.get(ext);
			if (extension == null) throw new IllegalArgumentException("Can not find extension for \"" + ext + "\"");
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

	public void validate() throws ParseException {
		checkNull(user, "username");
		checkNull(password, "password");
		checkNull(dialect, "dialect");
		checkNull(jdbcUri, "jdbcUri");
		checkNull(driverClass, "driverClass");
		checkNull(patchDirs, "patchDirs");
	}

	private void checkNull(Object what, String key) throws ParseException {
		if (what == null) {
			throw new ParseException("Property " + key + " in not set for configuration - " + id, 0);
		}
	}

	public String getPatchDirs() {
		return patchDirs;
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder();
		tsb.add("id", id);
		tsb.add("driverClass", driverClass).add("jdbcUri", jdbcUri);
		tsb.add("user", user).add("password", "****");
		tsb.add("patchDirs", patchDirs).add("encoding", patchEncoding);
		tsb.add("strategy", applyStarters);
		return tsb.toString();
	}

	public boolean isInteractivePasswordAllowed() {
		return true; // TODO move it into configuration ????
	}
}
