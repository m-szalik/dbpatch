package org.jsoftware.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoftware.config.Patch;

public class NamePatchScaner extends SimplePatchScaner {

	@Override
	protected void sortAll(List<Patch> allPatchList) {
		Collections.sort(allPatchList, new Comparator<Patch>() {
			public int compare(Patch o1, Patch o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	@Override
	protected void sortDirectory(List<Patch> dirPatchList) {
	}

}
