package org.jsoftware.impl;

import org.jsoftware.config.AbstractPatch;
import org.jsoftware.config.Patch;
import org.jsoftware.config.PatchScanner;
import org.jsoftware.impl.commons.FilenameUtils;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract directory scanner that looks for patch files.
 */
public abstract class SimplePatchScanner implements PatchScanner {
    private final Log log = LogFactory.getInstance();

    public List<Patch> scan(File baseDir, String[] paths) throws DuplicatePatchNameException, IOException {
        List<DirMask> dirMasks = parsePatchDirs(baseDir, paths);
        LinkedList<Patch> list = new LinkedList<Patch>();
        for (DirMask dm : dirMasks) {
            log.debug("Scan for patches " + dm.getDir().getAbsolutePath() + " with " + dm.getMask());
            LinkedList<Patch> dirList = new LinkedList<Patch>();
            File[] fList = dm.getDir().listFiles(new WildcardMaskFileFilter(dm.getMask()));
            for (File f : fList) {
                Patch p = new Patch();
                p.setFile(f);
                p.setName(AbstractPatch.normalizeName(f.getName()));
                dirList.add(p);
            }
            sortDirectory(dirList);
            for (Patch patch1 : dirList) {
                for (Patch patch2 : list) {
                    if (patch1.getName().equals(patch2.getName())) {
                        throw new DuplicatePatchNameException(this, patch1, patch2);
                    }
                }
                list.add(patch1);
            }
        }
        sortAll(list);
        return list;
    }

    @Override
    public File findRollbackFile(File baseDir, String[] paths, Patch patch) throws DuplicatePatchNameException, IOException {
        List<DirMask> dirMasks = parsePatchDirs(baseDir, paths);
        for (DirMask dm : dirMasks) {
            log.debug("Scan for rollback of '" + patch.getName() + "' " + dm.getDir().getAbsolutePath() + " with " + dm.getMask());
            File[] fList = dm.getDir().listFiles(new WildcardMaskFileFilter(dm.getMask()));
            for (File f : fList) {
                String rn = AbstractPatch.normalizeName(f.getName());
                if (rn.equals(patch.getName())) {
                    return f;
                }
            }
        }
        return null;
    }


    protected abstract void sortDirectory(List<Patch> dirPatchList);

    protected abstract void sortAll(List<Patch> allPatchList);


    List<DirMask> parsePatchDirs(File basePath, String[] dirs) throws IOException {
        List<DirMask> dirMasks = new LinkedList<DirMask>();
        for (String s : dirs) {
            s = s.trim();
            if (s.length() == 0) {
                log.debug(" - entry skipped");
                continue;
            }
            File f = new File(basePath, s);
            String absolute = f.getAbsolutePath();
            if (!absolute.contains("*") && !absolute.contains("?")) {
                if (f.isDirectory()) {
                    DirMask dm = new DirMask(f);
                    dirMasks.add(dm);
                    log.debug(" + entry directory " + dm);
                } else {
                    DirMask dm = new DirMask(f.getParentFile());
                    dm.setMask(f.getName());
                    dirMasks.add(dm);
                    log.debug(" + single file " + dm);
                }
            } else {
                String wch = f.getName();
                DirMask dm = new DirMask(f.getParentFile().getCanonicalFile());
                dm.setMask(wch);
                dirMasks.add(dm);
                log.debug(" + dir with special character " + dm);
            }
        }
        for (DirMask dm : dirMasks) {
            dm.validate();
        }
        return dirMasks;
    }


    static class WildcardMaskFileFilter implements FileFilter {
        private final String mask;

        public WildcardMaskFileFilter(String mask) {
            this.mask = mask;
        }

        public boolean accept(File pathname) {
            String fn = pathname.getName();
            return FilenameUtils.wildcardMatchOnSystem(fn, mask);
        }
    }

}

class DirMask {
    private final File dir;
    private String mask = "*.sql";

    public DirMask(File dir) {
        this.dir = dir;
    }

    public void validate() throws IllegalArgumentException {
        if (!dir.exists()) {
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

    @Override
    public String toString() {
        return "(" + dir.getAbsolutePath() + ":" + getMask() + ")";
    }

}
