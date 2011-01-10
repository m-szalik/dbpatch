package org.jsoftware.log;

import java.io.PrintStream;

public class LogFactory {
	private static Log instance;
		
	public static void initLocal() {
		LogLocalImpl l = new LogLocalImpl();
		l.currentLevel = Level.DEBUG;
		String logStr = System.getProperty("maven.dbpatch.log");
		if (logStr != null && logStr.trim().length() > 0) {
			logStr = logStr.trim().toUpperCase();
			l.currentLevel = Level.valueOf(logStr);
		}
		instance = l;
	}
	
	public static void initMaven(final org.apache.maven.plugin.logging.Log mlog) {
		instance = new Log() {
			public void warn(String msg) {
				mlog.warn(msg);
			}
			public void info(String msg) {
				mlog.info(msg);
			}
			public void fatal(String msg) {
				mlog.error(msg);
			}
			public void debug(String msg) {
				mlog.debug(msg);
			}
			public void warn(String msg, Throwable e) {
				mlog.warn(msg, e);
			};
			public void fatal(String msg, Throwable e) {
				mlog.error(msg, e);
			};
		};
	}
	
	private LogFactory() {
	}
	
	
	
	public static Log getInstance() {
		if (instance == null) {
			initLocal();
		}
		return instance;
	}
	
}

class LogLocalImpl implements Log{
	Level currentLevel;
	
	public void debug(String msg) {
		log(Level.DEBUG, msg, null);
	}

	public void info(String msg) {
		log(Level.INFO, msg, null);
	}
	
	public void warn(String msg) {
		log(Level.WARN, msg, null);
	}
	
	public void fatal(String msg) {
		log(Level.FATAL, msg, null);
	}
	
	public void warn(String msg, Throwable e) {
		log(Level.WARN, msg, e);
	}
	
	public void fatal(String msg, Throwable e) {
		log(Level.FATAL, msg, e);
	}
	
	private void log(Level lev, String msg, Throwable throwable) {
		PrintStream stream = System.out;
		if (lev.getPrio() >= currentLevel.getPrio()) {
			stream.println(lev.name() + " " + msg);
			if (throwable != null) {
				throwable.printStackTrace(stream);
			}
		}
	}
}
