package com.stericson.RootShell;

import android.util.Log;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootShell.execution.Shell.ShellContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import me.zhanghai.android.materialprogressbar.R;

public class RootShell {
    public static boolean debugMode = false;
    public static int defaultCommandTimeout = 20000;
    public static boolean handlerEnabled = true;
    public static final String version = "RootShell v1.3";

    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$stericson$RootShell$RootShell$LogLevel = new int[LogLevel.values().length];

        static {
            try {
                $SwitchMap$com$stericson$RootShell$RootShell$LogLevel[LogLevel.VERBOSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$stericson$RootShell$RootShell$LogLevel[LogLevel.ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$stericson$RootShell$RootShell$LogLevel[LogLevel.DEBUG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$stericson$RootShell$RootShell$LogLevel[LogLevel.WARN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum LogLevel {
        VERBOSE,
        ERROR,
        DEBUG,
        WARN
    }

    public static void closeAllShells() throws IOException {
        Shell.closeAll();
    }

    public static void closeCustomShell() throws IOException {
        Shell.closeCustomShell();
    }

    public static void closeShell(boolean root) throws IOException {
        if (root) {
            Shell.closeRootShell();
        } else {
            Shell.closeShell();
        }
    }

    public static boolean exists(String file) {
        return exists(file, false);
    }

    public static boolean exists(String file, boolean isDir) {
        final List<String> result = new ArrayList();
        String cmdToExecute = "ls " + (isDir ? "-d " : " ");
        Command command = new Command(0, false, new String[]{cmdToExecute + file}) {
            public void commandOutput(int id, String line) {
                RootShell.log(line);
                result.add(line);
                super.commandOutput(id, line);
            }
        };
        try {
            getShell(false).add(command);
            commandWait(getShell(false), command);
            for (String line : result) {
                if (line.trim().equals(file)) {
                    return true;
                }
            }
            result.clear();
            try {
                getShell(true).add(command);
                commandWait(getShell(true), command);
                List<String> final_result = new ArrayList();
                final_result.addAll(result);
                for (String line2 : final_result) {
                    if (line2.trim().equals(file)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static List<String> findBinary(String binaryName) {
        return findBinary(binaryName, null);
    }

    public static List<String> findBinary(String binaryName, List<String> searchPaths) {
        String path;
        final List<String> foundPaths = new ArrayList();
        boolean found = false;
        if (searchPaths == null) {
            searchPaths = getPath();
        }
        log("Checking for " + binaryName);
        try {
            for (String path2 : searchPaths) {
                if (!path2.endsWith("/")) {
                    path2 = path2 + "/";
                }
                final String currentPath = path2;
                final String str = binaryName;
                Command cc = new Command(0, false, new String[]{"stat " + path2 + binaryName}) {
                    public void commandOutput(int id, String line) {
                        if (line.contains("File: ") && line.contains(str)) {
                            foundPaths.add(currentPath);
                            RootShell.log(str + " was found here: " + currentPath);
                        }
                        RootShell.log(line);
                        super.commandOutput(id, line);
                    }
                };
                getShell(false).add(cc);
                commandWait(getShell(false), cc);
            }
            found = !foundPaths.isEmpty();
        } catch (Exception e) {
            log(binaryName + " was not found, more information MAY be available with Debugging on.");
        }
        if (!found) {
            log("Trying second method");
            for (String path22 : searchPaths) {
                if (!path22.endsWith("/")) {
                    path22 = path22 + "/";
                }
                if (exists(path22 + binaryName)) {
                    log(binaryName + " was found here: " + path22);
                    foundPaths.add(path22);
                } else {
                    log(binaryName + " was NOT found here: " + path22);
                }
            }
        }
        Collections.reverse(foundPaths);
        return foundPaths;
    }

    public static Shell getCustomShell(String shellPath, int timeout) throws IOException, TimeoutException, RootDeniedException {
        return getCustomShell(shellPath, timeout);
    }

    public static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }

    public static Shell getShell(boolean root, int timeout, ShellContext shellContext, int retry) throws IOException, TimeoutException, RootDeniedException {
        if (root) {
            return Shell.startRootShell(timeout, shellContext, retry);
        }
        return Shell.startShell(timeout);
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

    public static boolean isAccessGiven() {
        final Set<String> ID = new HashSet();
        try {
            log("Checking for Root access");
            Command command = new Command(158, false, new String[]{"id"}) {
                public void commandOutput(int id, String line) {
                    if (id == 158) {
                        ID.addAll(Arrays.asList(line.split(" ")));
                    }
                    super.commandOutput(id, line);
                }
            };
            Shell.startRootShell().add(command);
            commandWait(Shell.startRootShell(), command);
            for (String userid : ID) {
                log(userid);
                if (userid.toLowerCase().contains("uid=0")) {
                    log("Access Given");
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isBusyboxAvailable() {
        return findBinary("busybox").size() > 0;
    }

    public static boolean isRootAvailable() {
        return findBinary("su").size() > 0;
    }

    public static void log(String msg) {
        log(null, msg, LogLevel.DEBUG, null);
    }

    public static void log(String TAG, String msg) {
        log(TAG, msg, LogLevel.DEBUG, null);
    }

    public static void log(String msg, LogLevel type, Exception e) {
        log(null, msg, type, e);
    }

    public static boolean islog() {
        return debugMode;
    }

    public static void log(String TAG, String msg, LogLevel type, Exception e) {
        if (msg != null && !msg.equals(BuildConfig.FLAVOR) && debugMode) {
            if (TAG == null) {
                TAG = version;
            }
            switch (AnonymousClass4.$SwitchMap$com$stericson$RootShell$RootShell$LogLevel[type.ordinal()]) {
                case R.styleable.View_android_focusable /*1*/:
                    Log.v(TAG, msg);
                    return;
                case R.styleable.View_paddingStart /*2*/:
                    Log.e(TAG, msg, e);
                    return;
                case R.styleable.View_paddingEnd /*3*/:
                    Log.d(TAG, msg);
                    return;
                case R.styleable.View_theme /*4*/:
                    Log.w(TAG, msg);
                    return;
                default:
                    return;
            }
        }
    }

    private static void commandWait(Shell shell, Command cmd) throws Exception {
        while (!cmd.isFinished()) {
            log(version, shell.getCommandQueuePositionString(cmd));
            log(version, "Processed " + cmd.totalOutputProcessed + " of " + cmd.totalOutput + " output from command.");
            synchronized (cmd) {
                try {
                    if (!cmd.isFinished()) {
                        cmd.wait(2000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!(cmd.isExecuting() || cmd.isFinished())) {
                Exception e2;
                if (!shell.isExecuting && !shell.isReading) {
                    log(version, "Waiting for a command to be executed in a shell that is not executing and not reading! \n\n Command: " + cmd.getCommand());
                    e2 = new Exception();
                    e2.setStackTrace(Thread.currentThread().getStackTrace());
                    e2.printStackTrace();
                } else if (!shell.isExecuting || shell.isReading) {
                    log(version, "Waiting for a command to be executed in a shell that is not reading! \n\n Command: " + cmd.getCommand());
                    e2 = new Exception();
                    e2.setStackTrace(Thread.currentThread().getStackTrace());
                    e2.printStackTrace();
                } else {
                    log(version, "Waiting for a command to be executed in a shell that is executing but not reading! \n\n Command: " + cmd.getCommand());
                    e2 = new Exception();
                    e2.setStackTrace(Thread.currentThread().getStackTrace());
                    e2.printStackTrace();
                }
            }
        }
    }
}
