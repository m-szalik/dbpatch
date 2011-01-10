package org.jsoftware.config;

import java.util.List;

public interface PatchScaner {
	
	public interface DirectoryInjectionScaner extends PatchScaner {
		void setPatchDirs(String patchDirs);
	}
	
	List<Patch> scan();

}
