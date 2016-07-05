package com.stericson.RootTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootShell.execution.Shell.ShellContext;
import com.stericson.RootTools.containers.Mount;
import com.stericson.RootTools.containers.Permissions;
import com.stericson.RootTools.containers.Symlink;
import com.stericson.RootTools.internal.Remounter;
import com.stericson.RootTools.internal.RootToolsInternalMethods;
import com.stericson.RootTools.internal.Runner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import me.zhanghai.android.materialprogressbar.R;

public final class RootTools {
    public static boolean debugMode = false;
    public static int default_Command_Timeout = 20000;
    public static boolean handlerEnabled = true;
    private static RootToolsInternalMethods rim = null;
    public static String utilPath;

    public static void setRim(RootToolsInternalMethods rim) {
        rim = rim;
    }

    private static final RootToolsInternalMethods getInternals() {
        if (rim != null) {
            return rim;
        }
        RootToolsInternalMethods.getInstance();
        return rim;
    }

    public static boolean checkUtil(String util) {
        return getInternals().checkUtil(util);
    }

    public static void closeAllShells() throws IOException {
        RootShell.closeAllShells();
    }

    public static void closeCustomShell() throws IOException {
        RootShell.closeCustomShell();
    }

    public static void closeShell(boolean root) throws IOException {
        RootShell.closeShell(root);
    }

    public static boolean copyFile(String source, String destination, boolean remountAsRw, boolean preserveFileAttributes) {
        return getInternals().copyFile(source, destination, remountAsRw, preserveFileAttributes);
    }

    public static boolean deleteFileOrDirectory(String target, boolean remountAsRw) {
        return getInternals().deleteFileOrDirectory(target, remountAsRw);
    }

    public static boolean exists(String file) {
        return exists(file, false);
    }

    public static boolean exists(String file, boolean isDir) {
        return RootShell.exists(file, isDir);
    }

    public static void fixUtil(String util, String utilPath) {
        getInternals().fixUtil(util, utilPath);
    }

    public static boolean fixUtils(String[] utils) throws Exception {
        return getInternals().fixUtils(utils);
    }

    public static List<String> findBinary(String binaryName) {
        return RootShell.findBinary(binaryName);
    }

    public static String getBusyBoxVersion(String path) {
        return getInternals().getBusyBoxVersion(path);
    }

    public static String getBusyBoxVersion() {
        return getBusyBoxVersion(BuildConfig.FLAVOR);
    }

    public static List<String> getBusyBoxApplets() throws Exception {
        return getBusyBoxApplets(BuildConfig.FLAVOR);
    }

    public static List<String> getBusyBoxApplets(String path) throws Exception {
        return getInternals().getBusyBoxApplets(path);
    }

    public static Shell getCustomShell(String shellPath, int timeout) throws IOException, TimeoutException, RootDeniedException {
        return RootShell.getCustomShell(shellPath, timeout);
    }

    public static Shell getCustomShell(String shellPath) throws IOException, TimeoutException, RootDeniedException {
        return getCustomShell(shellPath, 10000);
    }

    public static Permissions getFilePermissionsSymlinks(String file) {
        return getInternals().getFilePermissionsSymlinks(file);
    }

    public static String getInode(String file) {
        return getInternals().getInode(file);
    }

    public static ArrayList<Mount> getMounts() throws Exception {
        return getInternals().getMounts();
    }

    public static String getMountedAs(String path) throws Exception {
        return getInternals().getMountedAs(path);
    }

    public static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }

    public static Shell getShell(boolean root, int timeout, ShellContext shellContext, int retry) throws IOException, TimeoutException, RootDeniedException {
        return RootShell.getShell(root, timeout, shellContext, retry);
    }

    public static Shell getShell(boolean root, int timeout, ShellContext shellContext) throws IOException, TimeoutException, RootDeniedException {
        return getShell(root, timeout, shellContext, 3);
    }

    public static Shell getShell(boolean root, ShellContext shellContext) throws IOException, TimeoutException, RootDeniedException {
        return getShell(root, 0, shellContext, 3);
    }

    public static Shell getShell(boolean root, int timeout) throws IOException, TimeoutException, RootDeniedException {
        return getShell(root, timeout, Shell.defaultContext, 3);
    }

    public static Shell getShell(boolean root) throws IOException, TimeoutException, RootDeniedException {
        return getShell(root, 0);
    }

    public static long getSpace(String path) {
        return getInternals().getSpace(path);
    }

    public static String getSymlink(String file) {
        return getInternals().getSymlink(file);
    }

    public static ArrayList<Symlink> getSymlinks(String path) throws Exception {
        return getInternals().getSymlinks(path);
    }

    public static String getWorkingToolbox() {
        return getInternals().getWorkingToolbox();
    }

    public static boolean hasEnoughSpaceOnSdCard(long updateSize) {
        return getInternals().hasEnoughSpaceOnSdCard(updateSize);
    }

    public static boolean hasUtil(String util, String box) {
        return getInternals().hasUtil(util, box);
    }

    public static boolean installBinary(Context context, int sourceId, String destName, String mode) {
        return getInternals().installBinary(context, sourceId, destName, mode);
    }

    public static boolean installBinary(Context context, int sourceId, String binaryName) {
        return installBinary(context, sourceId, binaryName, "700");
    }

    public static boolean hasBinary(Context context, String binaryName) {
        return getInternals().isBinaryAvailable(context, binaryName);
    }

    public static boolean isAppletAvailable(String applet, String path) {
        return getInternals().isAppletAvailable(applet, path);
    }

    public static boolean isAppletAvailable(String applet) {
        return isAppletAvailable(applet, BuildConfig.FLAVOR);
    }

    public static boolean isAccessGiven() {
        return RootShell.isAccessGiven();
    }

    public static boolean isBusyboxAvailable() {
        return RootShell.isBusyboxAvailable();
    }

    public static boolean isNativeToolsReady(int nativeToolsId, Context context) {
        return getInternals().isNativeToolsReady(nativeToolsId, context);
    }

    public static boolean isProcessRunning(String processName) {
        return getInternals().isProcessRunning(processName);
    }

    public static boolean isRootAvailable() {
        return RootShell.isRootAvailable();
    }

    public static boolean killProcess(String processName) {
        return getInternals().killProcess(processName);
    }

    public static void offerBusyBox(Activity activity) {
        getInternals().offerBusyBox(activity);
    }

    public static Intent offerBusyBox(Activity activity, int requestCode) {
        return getInternals().offerBusyBox(activity, requestCode);
    }

    public static void offerSuperUser(Activity activity) {
        getInternals().offerSuperUser(activity);
    }

    public static Intent offerSuperUser(Activity activity, int requestCode) {
        return getInternals().offerSuperUser(activity, requestCode);
    }

    public static boolean remount(String file, String mountType) {
        return new Remounter().remount(file, mountType);
    }

    public static void restartAndroid() {
        log("Restart Android");
        killProcess("zygote");
    }

    public static void runBinary(Context context, String binaryName, String parameter) {
        new Runner(context, binaryName, parameter).start();
    }

    public static void runShellCommand(Shell shell, Command command) throws IOException {
        shell.add(command);
    }

    public static void log(String msg) {
        log(null, msg, 3, null);
    }

    public static void log(String TAG, String msg) {
        log(TAG, msg, 3, null);
    }

    public static void log(String msg, int type, Exception e) {
        log(null, msg, type, e);
    }

    public static boolean islog() {
        return debugMode;
    }

    public static void log(String TAG, String msg, int type, Exception e) {
        if (msg != null && !msg.equals(BuildConfig.FLAVOR) && debugMode) {
            if (TAG == null) {
                TAG = Constants.TAG;
            }
            switch (type) {
                case R.styleable.View_android_focusable /*1*/:
                    Log.v(TAG, msg);
                    return;
                case R.styleable.View_paddingStart /*2*/:
                    Log.e(TAG, msg, e);
                    return;
                case R.styleable.View_paddingEnd /*3*/:
                    Log.d(TAG, msg);
                    return;
                default:
                    return;
            }
        }
    }
}
