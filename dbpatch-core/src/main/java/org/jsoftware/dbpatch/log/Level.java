package org.jsoftware.dbpatch.log;

public enum Level {
    TRACE(0),
    DEBUG(5),
    INFO(10),
    WARN(15),
    FATAL(20);

    private final int priority;

    Level(int p) {
        priority = p;
    }

    public int getPriority() {
        return priority;
    }
}
