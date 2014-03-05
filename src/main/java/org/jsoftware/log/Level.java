package org.jsoftware.log;

public enum Level {
    DEBUG(5),
    INFO(10),
    WARN(15),
    FATAL(20);

    private Level(int p) {
        priority = p;
    }

    public int getPriority() {
        return priority;
    }

    private final int priority;
}
