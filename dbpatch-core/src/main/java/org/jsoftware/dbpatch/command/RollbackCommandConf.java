package org.jsoftware.dbpatch.command;

import java.io.Serializable;

public class RollbackCommandConf implements Serializable {
    private static final long serialVersionUID = -7838793234441198861L;

    public enum Action {
        SINGLE, STOP_ON
    }

    private final Action action;
    private final String patchName;

    public RollbackCommandConf(Action action, String patchName) {
        this.action = action;
        this.patchName = patchName;
    }

    public Action getAction() {
        return action;
    }

    public String getPatchName() {
        return patchName;
    }
}
