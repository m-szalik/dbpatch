package org.jsoftware.log;

public enum Level {
	DEBUG(5),
	INFO(10),
	WARN(15), 
	FATAL(20);
	
	private Level(int p) {
		prio = p;
	}
	
	public int getPrio() {
		return prio;
	}
	
	private int prio;
}
