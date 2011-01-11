package org.jsoftware.config;

import java.util.List;

public interface PatchScaner {
	
	public interface ConfigurationEntryAware extends PatchScaner {
		void setConfigurationEntry(ConfigurationEntry ce);
	}
	
	List<Patch> scan();

}
