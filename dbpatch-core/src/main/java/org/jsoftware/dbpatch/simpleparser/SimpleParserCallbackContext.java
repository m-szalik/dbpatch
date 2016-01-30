package org.jsoftware.dbpatch.simpleparser;

public class SimpleParserCallbackContext {
    private final String input;
    private int endp;
    private int startp;

    SimpleParserCallbackContext(String input) {
        this.input = input;
    }

    void setEndp(int pos) {
        this.endp = pos;
    }

    void setStartp(int pos) {
        this.startp = pos;
    }

    public int getStartp() {
        return startp;
    }

    public int getEndp() {
        return endp;
    }

    public String getTextBefore() {
        return input.substring(startp, endp);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + startp + "," + endp + "]";
    }
}
