package org.jsoftware.config;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import org.jsoftware.config.Patch.DbState;

public class LastApplyStrategy implements ApplyStrategy {

	public List<Patch> filter(Connection con, List<Patch> patches) {
		int last = 0;
		for(int i=0; i<patches.size(); i++) {
			if (patches.get(i).getDbState() == DbState.COMMITED) {
				last = i;
			}
		}
		return (List<Patch>) ((last >= patches.size()) ? Collections.emptyList() : patches.subList(last+1, patches.size()));
	}

}
