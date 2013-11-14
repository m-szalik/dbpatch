package org.jsoftware.impl;

import org.jsoftware.command.AbstractCommand;
import org.jsoftware.command.CommandFailureException;
import org.jsoftware.config.Patch;

public class DuplicatePatchNameException extends CommandFailureException {
	private static final long serialVersionUID = 5221931112583803769L;

	public DuplicatePatchNameException(Object source, Patch patch1, Patch patch2) {
		super(source, "Duplicate patch name", "Patches name \"" + patch1.getName() + "\" conflict between " + patch1.getFile().getAbsolutePath() + " and " + patch2.getFile().getAbsolutePath());
	}

}
