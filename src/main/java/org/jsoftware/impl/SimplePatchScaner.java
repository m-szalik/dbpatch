package org.jsoftware.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.jsoftware.config.ConfigurationEntry;
import org.jsoftware.config.Patch;
import org.jsoftware.config.PatchScaner;
import org.jsoftware.impl.commons.FilenameUtils;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

public class SimplePatchScaner implements PatchScaner, PatchScaner.ConfigurationEntryAware {
	private List<DirMask> dirMasks;
	private Log log = LogFactory.getInstance();
	
	public List<Patch> scan() {	
		LinkedList<Patch> list = new LinkedList<Patch>();
		for(DirMask dm : dirMasks) {
			log.debug("Scan " + dm.getDir().getAbsolutePath() + " for " + dm.getMask());
			LinkedList<Patch> dirList = new LinkedList<Patch>();
			File[] flist = dm.getDir().listFiles(new WildchardMaskFileFilter(dm.getMask()));
			for (File f : flist) {
				Patch p = new Patch();
				p.setFile(f);
				p.setName(f.getName().substring(0, f.getName().length() - 4));
				dirList.add(p);
			}
			Collections.sort(dirList, new Comparator<Patch>() {
				public int compare(Patch o1, Patch o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			list.addAll(dirList);
		}
		return list;

	}

	
	private void parsePatchDirs(String patchDirs) {
		List<DirMask> dirMasks = new LinkedList<DirMask>();
		for(String s : patchDirs.split(",")) {
			s = s.trim();
			if (s.length() == 0) continue;
			File f = new File(s);
			if (f.isDirectory()) {
				dirMasks.add(new DirMask(f));
			} else {
				String wch = f.getName();
				DirMask dm = new DirMask(f.getParentFile());
				dm.setMask(wch);
			}
		}
		for(DirMask dm : dirMasks) {
			dm.validate();
		}
		this.dirMasks = dirMasks;
		StringBuilder sb = new StringBuilder();
		for(DirMask dm : dirMasks) {
			sb.append(dm.getDir().getAbsolutePath()).append(" with ").append(dm.getMask());
			sb.append(", ");
		}
		log.debug("ScanDirs configValue is " + patchDirs + " that creates " + sb.toString().trim());
	}
	
	
	public void setConfigurationEntry(ConfigurationEntry ce) {
		parsePatchDirs(ce.getPatchDirs());
	}
	
	class WildchardMaskFileFilter implements FileFilter {
		private String mask;
		public WildchardMaskFileFilter(String mask) {
			this.mask = mask;
		}

		public boolean accept(File pathname) {
			String fn = pathname.getName();
			boolean b = FilenameUtils.wildcardMatchOnSystem(fn, mask);
			log.debug("Check WildchardMaskFileFilter - " + fn + " is " + b);
			return b;
		}
	}

}

class DirMask {
	private File dir;
	private String mask = "*.sql";
	
	public DirMask(File dir) {
		this.dir = dir;
	}
	
	public void validate() throws IllegalArgumentException {
		if (! dir.exists()) {
			throw new IllegalArgumentException("Directory " + dir.getPath() + " does not exist.");
		}
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
	public File getDir() {
		return dir;
	}
	
	public String getMask() {
		return mask;
	}
	
}
