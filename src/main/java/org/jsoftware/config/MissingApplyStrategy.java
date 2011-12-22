package org.jsoftware.config;

import java.io.Serializable;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.jsoftware.config.Patch.DbState;

public class MissingApplyStrategy implements ApplyStrategy, Serializable {
	private static final long serialVersionUID = -8434361234942313924L;

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
