package org.jsoftware.command;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.Patch;
import org.jsoftware.config.PatchScanner;
import org.jsoftware.impl.DuplicatePatchNameException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * Show list of patches
 * @author szalik
 */
public abstract class AbstractListCommand<P extends AbstractPatch> extends AbstractSingleConfDbPatchCommand {
	
	/**
	 * @return patches to apply
	 * @throws java.io.IOException
	 * @throws java.sql.SQLException
	 * @throws org.jsoftware.impl.DuplicatePatchNameException
	 */
	private List<Patch> generatePatchListAll() throws IOException, SQLException, DuplicatePatchNameException {
		PatchScanner scanner = configurationEntry.getPatchScanner();
		List<Patch> patches = scanner.scan(directory, configurationEntry.getPatchDirs().split(","));
		manager.updateStateObjectAll(patches);
		return patches;
	}

    abstract protected List<P> generateList(List<Patch> inList) throws IOException, SQLException, DuplicatePatchNameException;

    public List<P> getList() throws DuplicatePatchNameException, SQLException, IOException {
        return generateList(generatePatchListAll());
    }



}
