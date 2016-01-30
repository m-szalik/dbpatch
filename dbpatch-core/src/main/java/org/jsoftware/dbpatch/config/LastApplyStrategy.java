package org.jsoftware.dbpatch.config;


import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * Apply only new patches.
 *
 * @author szalik
 */
public class LastApplyStrategy implements ApplyStrategy {

    @SuppressWarnings("unchecked")
    public List<Patch> filter(Connection con, List<Patch> patches) {
        int last = 0;
        for (int i = 0; i < patches.size(); i++) {
            if (patches.get(i).getDbState() == AbstractPatch.DbState.COMMITTED) {
                last = i;
            }
        }
        return (List<Patch>) ((last >= patches.size()) ? Collections.emptyList() : patches.subList(last + 1, patches.size()));
    }

}
