package com.stericson.RootTools.internal;

import com.stericson.RootTools.containers.Mount;
import com.stericson.RootTools.containers.Permissions;
import com.stericson.RootTools.containers.Symlink;
import java.util.ArrayList;
import java.util.regex.Pattern;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class InternalVariables {
    protected static final String PS_REGEX = "^\\S+\\s+([0-9]+).*$";
    protected static String busyboxVersion;
    protected static boolean found = false;
    protected static String getSpaceFor;
    protected static String inode = BuildConfig.FLAVOR;
    protected static ArrayList<Mount> mounts;
    protected static boolean nativeToolsReady = false;
    protected static Permissions permissions;
    protected static String pid_list = BuildConfig.FLAVOR;
    protected static boolean processRunning = false;
    protected static Pattern psPattern = Pattern.compile(PS_REGEX);
    protected static String[] space;
    protected static ArrayList<Symlink> symlinks;
}
