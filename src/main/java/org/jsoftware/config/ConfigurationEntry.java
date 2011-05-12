package org.jsoftware.config;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jsoftware.config.dialect.DefaultDialect;
import org.jsoftware.config.dialect.Dialect;
import org.jsoftware.config.dialect.DialectFinder;
import org.jsoftware.impl.DefaultPatchParser;
import org.jsoftware.impl.DirectoryPatchScaner;
import org.jsoftware.impl.PatchParser;
import org.jsoftware.impl.extension.Extension;
import org.jsoftware.impl.extension.TkExtensionAndStrategy;

public class ConfigurationEntry implements Serializable, Cloneable {
	private static Map<String, ApplyStrategy> applayStartegies;
	private static Map<String, Extension> availableExtensions;
	
	static {
		applayStartegies = new HashMap<String, ApplyStrategy>();
		applayStartegies.put("missing", new MissingApplyStrategy());
		applayStartegies.put("last", new LastApplyStrategy());
		applayStartegies.put("tk", new TkExtensionAndStrategy());
		
		availableExtensions = new HashMap<String, Extension>();
		availableExtensions.put("tk", new TkExtensionAndStrategy());
	}
	private static final long serialVersionUID = 1L;
	private String id;
	private String jdbcUri;
	private String driverClass;
	private String user;
	private String password;
	private Dialect dialect;
	private String patchDirs;
	private PatchScaner patchScaner;
	private PatchParser patchParser;
	private ApplyStrategy applayStartegy;
	private Collection<Extension> extensions;
	private Charset patchEncoding;
	
	
	public ConfigurationEntry() {
		this("maven:pom.xml");
	}
	
	ConfigurationEntry(String id) {
		this.id = id;
		dialect = new DefaultDialect();
		patchScaner = new DirectoryPatchScaner();
		patchParser = new DefaultPatchParser();
		applayStartegy = new MissingApplyStrategy();
		extensions = Collections.emptySet();
		patchEncoding = Charset.defaultCharset();
	}
	
	public Charset getPatchEncoding() {
		return patchEncoding;
	}
	
	public void setPatchEncoding(String patchEncoding) {
		this.patchEncoding = Charset.forName(patchEncoding);
	}
	
	public void setEncoding(String patchEncoding) {
		setPatchEncoding(patchEncoding);
	}
	
	public ApplyStrategy getApplayStartegy() {
		return applayStartegy;
	}
	
	public void setApplayStartegy(String applayStartegy) {
		applayStartegy = applayStartegy.toLowerCase().trim();
		this.applayStartegy = applayStartegies.get(applayStartegy);
		if (this.applayStartegy == null) {
			throw new IllegalArgumentException("Can not find applyStrategy for \"" + applayStartegy + "\"");
		}
	}
	
	public void setStrategy(String startegy) {
		setApplayStartegy(startegy);
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

	public PatchScaner getPatchScaner() {
		return patchScaner;
	}
	
	public void setPatchScaner(PatchScaner patchScaner) {
		this.patchScaner = patchScaner;
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
		tsb.add("strategy", applayStartegy);
		return tsb.toString();
	}
}
