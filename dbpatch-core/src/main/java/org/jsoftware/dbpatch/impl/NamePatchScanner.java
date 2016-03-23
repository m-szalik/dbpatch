package org.jsoftware.dbpatch.impl;

import org.jsoftware.dbpatch.config.Patch;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts patches by name from all directories
 * @author szalik
 */
public class NamePatchScanner extends SimplePatchScanner {


    protected void sortAll(List<Patch> allPatchList) {
        Collections.sort(allPatchList, new Comparator<Patch>() {
            public int compare(Patch o1, Patch o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }


    protected void sortDirectory(List<Patch> dirPatchList) {
    }

}
