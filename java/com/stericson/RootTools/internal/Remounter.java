package com.stericson.RootTools.internal;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootTools.Constants;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.containers.Mount;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Remounter {
    public boolean remount(String file, String mountType) {
        if (file.endsWith("/") && !file.equals("/")) {
            file = file.substring(0, file.lastIndexOf("/"));
        }
        boolean foundMount = false;
        while (!foundMount) {
            try {
                Iterator i$ = RootTools.getMounts().iterator();
                while (i$.hasNext()) {
                    Mount mount = (Mount) i$.next();
                    RootTools.log(mount.getMountPoint().toString());
                    if (file.equals(mount.getMountPoint().toString())) {
                        foundMount = true;
                        break;
                    }
                }
                if (!foundMount) {
                    try {
                        file = new File(file).getParent();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            } catch (Exception e2) {
                if (RootTools.debugMode) {
                    e2.printStackTrace();
                }
                return false;
            }
        }
        Mount mountPoint = findMountPointRecursive(file);
        if (mountPoint != null) {
            RootTools.log(Constants.TAG, "Remounting " + mountPoint.getMountPoint().getAbsolutePath() + " as " + mountType.toLowerCase());
            if (!mountPoint.getFlags().contains(mountType.toLowerCase())) {
                try {
                    Command command = new Command(0, true, "busybox mount -o remount," + mountType.toLowerCase() + " " + mountPoint.getDevice().getAbsolutePath() + " " + mountPoint.getMountPoint().getAbsolutePath(), "toolbox mount -o remount," + mountType.toLowerCase() + " " + mountPoint.getDevice().getAbsolutePath() + " " + mountPoint.getMountPoint().getAbsolutePath(), "mount -o remount," + mountType.toLowerCase() + " " + mountPoint.getDevice().getAbsolutePath() + " " + mountPoint.getMountPoint().getAbsolutePath(), "/system/bin/toolbox mount -o remount," + mountType.toLowerCase() + " " + mountPoint.getDevice().getAbsolutePath() + " " + mountPoint.getMountPoint().getAbsolutePath());
                    Shell.startRootShell().add(command);
                    commandWait(command);
                } catch (Exception e3) {
                }
                mountPoint = findMountPointRecursive(file);
            }
            if (mountPoint != null) {
                RootTools.log(Constants.TAG, mountPoint.getFlags() + " AND " + mountType.toLowerCase());
                if (mountPoint.getFlags().contains(mountType.toLowerCase())) {
                    RootTools.log(mountPoint.getFlags().toString());
                    return true;
                }
                RootTools.log(mountPoint.getFlags().toString());
                return false;
            }
            RootTools.log("mount is null, file was: " + file + " mountType was: " + mountType);
        } else {
            RootTools.log("mount is null, file was: " + file + " mountType was: " + mountType);
        }
        return false;
    }

    private Mount findMountPointRecursive(String file) {
        try {
            ArrayList<Mount> mounts = RootTools.getMounts();
            File path = new File(file);
            while (path != null) {
                Iterator i$ = mounts.iterator();
                while (i$.hasNext()) {
                    Mount mount = (Mount) i$.next();
                    if (mount.getMountPoint().equals(path)) {
                        return mount;
                    }
                }
            }
            return null;
        } catch (IOException e) {
            if (RootTools.debugMode) {
                e.printStackTrace();
            }
            return null;
        } catch (Exception e2) {
            if (RootTools.debugMode) {
                e2.printStackTrace();
            }
            return null;
        }
    }

    private void commandWait(Command cmd) {
        synchronized (cmd) {
            try {
                if (!cmd.isFinished()) {
                    cmd.wait(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
