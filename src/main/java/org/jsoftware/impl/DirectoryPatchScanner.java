package org.jsoftware.impl;

import org.jsoftware.config.Patch;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts patches by name in directories
 * This is default behavior.
 */
public class DirectoryPatchScanner extends SimplePatchScanner implements Serializable {
    private static final long serialVersionUID = 2156150612939870399L;


    @Override
    protected void sortDirectory(List<Patch> dirPatchList) {
        Collections.sort(dirPatchList, new Comparator<Patch>() {
            public int compare(Patch o1, Patch o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }


    @Override
    protected void sortAll(List<Patch> allPatchList) {
    }

}
