package org.jsoftware.config;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.jsoftware.config.Patch.DbState;

public class MissingApplyStrategy implements ApplyStrategy {

	public List<Patch> filter(Connection con, List<Patch> patches) {
		LinkedList<Patch> patchesToApply = new LinkedList<Patch>();
		for(Patch p : patches) {
			if (p.getDbState() != DbState.COMMITED) {
				patchesToApply.add(p);
			}
		}
		return patchesToApply;
	}

}
