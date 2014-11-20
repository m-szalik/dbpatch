package org.jsoftware.config;

import java.sql.Connection;
import java.util.List;

/**
 * Decides which patches should by applied.
 *
 * @author szalik
 * @see MissingApplyStrategy
 */
public interface ApplyStrategy {

    /**
     * @param patches all detected patches
     * @return list of patches to apply
     */
    List<Patch> filter(Connection connection, List<Patch> patches);

}
