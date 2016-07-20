package com.stericson.RootShell.execution;

import android.content.Context;
import android.os.Build.VERSION;
import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.RootShell.LogLevel;
import com.stericson.RootShell.exceptions.RootDeniedException;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class Shell {
    private static Shell customShell = null;
    public static ShellContext defaultContext = ShellContext.NORMAL;
    private static Shell rootShell = null;
    private static Shell shell = null;
    private static String[] suVersion = new String[]{null, null};
    private static final String token = "F*D^W@#FGF";
    private boolean close = false;
    private final List<Command> commands = new ArrayList();
    private String error = BuildConfig.FLAVOR;
    private final BufferedReader errorStream;
    private Runnable input = new Runnable() {
        public void run() {
            while (true) {
                try {
                    synchronized (Shell.this.commands) {
                        while (!Shell.this.close && Shell.this.write >= Shell.this.commands.size()) {
                            Shell.this.isExecuting = false;
                            Shell.this.commands.wait();
                        }
                    }
                    if (Shell.this.write >= Shell.this.maxCommands) {
                        while (Shell.this.read != Shell.this.write) {
                            RootShell.log("Waiting for read and write to catch up before cleanup.");
                        }
                        Shell.this.cleanCommands();
                    }
                    if (Shell.this.write < Shell.this.commands.size()) {
                        Shell.this.isExecuting = true;
                        Command cmd = (Command) Shell.this.commands.get(Shell.this.write);
                        cmd.startExecution();
                        RootShell.log("Executing: " + cmd.getCommand() + " with context: " + Shell.this.shellContext);
                        Shell.this.outputStream.write(cmd.getCommand());
                        Shell.this.outputStream.write("\necho F*D^W@#FGF " + Shell.this.totalExecuted + " $?\n");
                        Shell.this.outputStream.flush();
                        Shell.this.write = Shell.this.write + 1;
                        Shell.this.totalExecuted = Shell.this.totalExecuted + 1;
                    } else if (Shell.this.close) {
                        Shell.this.isExecuting = false;
                        Shell.this.outputStream.write("\nexit 0\n");
                        Shell.this.outputStream.flush();
                        RootShell.log("Closing shell");
                        Shell.this.write = 0;
                        Shell.this.closeQuietly(Shell.this.outputStream);
                        return;
                    }
                } catch (IOException e) {
                    try {
                        RootShell.log(e.getMessage(), LogLevel.ERROR, e);
                        return;
                    } finally {
                        Shell.this.write = 0;
                        Shell.this.closeQuietly(Shell.this.outputStream);
                    }
                } catch (InterruptedException e2) {
                    RootShell.log(e2.getMessage(), LogLevel.ERROR, e2);
                    Shell.this.write = 0;
                    Shell.this.closeQuietly(Shell.this.outputStream);
                    return;
                }
            }
        }
    };
    private final BufferedReader inputStream;
    private boolean isCleaning = false;
    public boolean isClosed = false;
    public boolean isExecuting = false;
    public boolean isReading = false;
    private Boolean isSELinuxEnforcing = null;
    private int maxCommands = 5000;
    private Runnable output = new Runnable() {
        public void run() {
            Command command = null;
            while (true) {
                try {
                    if (!Shell.this.close || Shell.this.inputStream.ready() || Shell.this.read < Shell.this.commands.size()) {
                        Shell.this.isReading = false;
                        String outputLine = Shell.this.inputStream.readLine();
                        Shell.this.isReading = true;
                        if (outputLine != null) {
                            if (command == null) {
                                if (Shell.this.read < Shell.this.commands.size()) {
                                    command = (Command) Shell.this.commands.get(Shell.this.read);
                                } else if (Shell.this.close) {
                                    break;
                                }
                            }
                            int pos = outputLine.indexOf(Shell.token);
                            if (pos == -1) {
                                command.output(command.id, outputLine);
                            } else if (pos > 0) {
                                command.output(command.id, outputLine.substring(0, pos));
                            }
                            if (pos >= 0) {
                                String[] fields = outputLine.substring(pos).split(" ");
                                if (fields.length >= 2 && fields[1] != null) {
                                    int id = 0;
                                    try {
                                        id = Integer.parseInt(fields[1]);
                                    } catch (NumberFormatException e) {
                                    }
                                    int exitCode = -1;
                                    try {
                                        exitCode = Integer.parseInt(fields[2]);
                                    } catch (NumberFormatException e2) {
                                    }
                                    if (id == Shell.this.totalRead) {
                                        Shell.this.processErrors(command);
                                        int iterations = 0;
                                        while (command.totalOutput > command.totalOutputProcessed) {
                                            if (iterations == 0) {
                                                iterations++;
                                                RootShell.log("Waiting for output to be processed. " + command.totalOutputProcessed + " Of " + command.totalOutput);
                                            }
                                            try {
                                                synchronized (this) {
                                                    wait(2000);
                                                }
                                            } catch (Exception e3) {
                                                RootShell.log(e3.getMessage());
                                            }
                                        }
                                        RootShell.log("Read all output");
                                        command.setExitCode(exitCode);
                                        command.commandFinished();
                                        command = null;
                                        Shell.this.read = Shell.this.read + 1;
                                        Shell.this.totalRead = Shell.this.totalRead + 1;
                                    } else {
                                        continue;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                    try {
                        break;
                    } catch (Exception e4) {
                    }
                } catch (IOException e5) {
                    RootShell.log(e5.getMessage(), LogLevel.ERROR, e5);
                } finally {
                    Shell.this.closeQuietly(Shell.this.outputStream);
                    Shell.this.closeQuietly(Shell.this.errorStream);
                    Shell.this.closeQuietly(Shell.this.inputStream);
                    RootShell.log("Shell destroyed");
                    Shell.this.isClosed = true;
                    Shell.this.isReading = false;
                }
            }
            Shell.this.proc.waitFor();
            Shell.this.proc.destroy();
            while (Shell.this.read < Shell.this.commands.size()) {
                if (command == null) {
                    command = (Command) Shell.this.commands.get(Shell.this.read);
                }
                if (command.totalOutput < command.totalOutputProcessed) {
                    command.terminated("All output not processed!");
                    command.terminated("Did you forget the super.commandOutput call or are you waiting on the command object?");
                } else {
                    command.terminated("Unexpected Termination.");
                }
                command = null;
                Shell.this.read = Shell.this.read + 1;
            }
            Shell.this.read = 0;
        }
    };
    private final OutputStreamWriter outputStream;
    private final Process proc;
    private int read = 0;
    private ShellContext shellContext = ShellContext.NORMAL;
    private int shellTimeout = 25000;
    private ShellType shellType = null;
    private int totalExecuted = 0;
    private int totalRead = 0;
    private int write = 0;

    public enum ShellContext {
        NORMAL("normal"),
        SHELL("u:r:shell:s0"),
        SYSTEM_SERVER("u:r:system_server:s0"),
        SYSTEM_APP("u:r:system_app:s0"),
        PLATFORM_APP("u:r:platform_app:s0"),
        UNTRUSTED_APP("u:r:untrusted_app:s0"),
        RECOVERY("u:r:recovery:s0");
        
        private String value;

        private ShellContext(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum ShellType {
        NORMAL,
        ROOT,
        CUSTOM
    }

    protected static class Worker extends Thread {
        public int exit;
        public Shell shell;

        private Worker(Shell shell) {
            this.exit = -911;
            this.shell = shell;
        }

        public void run() {
            try {
                this.shell.outputStream.write("echo Started\n");
                this.shell.outputStream.flush();
                while (true) {
                    String line = this.shell.inputStream.readLine();
                    if (line == null) {
                        throw new EOFException();
                    } else if (!BuildConfig.FLAVOR.equals(line)) {
                        if ("Started".equals(line)) {
                            this.exit = 1;
                            setShellOom();
                            return;
                        }
                        this.shell.error = "unkown error occured.";
                    }
                }
            } catch (IOException e) {
                this.exit = -42;
                if (e.getMessage() != null) {
                    this.shell.error = e.getMessage();
                } else {
                    this.shell.error = "RootAccess denied?.";
                }
            }
        }

        private void setShellOom() {
            try {
                Field field;
                Class<?> processClass = this.shell.proc.getClass();
                try {
                    field = processClass.getDeclaredField("pid");
                } catch (NoSuchFieldException e) {
                    field = processClass.getDeclaredField("id");
                }
                field.setAccessible(true);
                this.shell.outputStream.write("(echo -17 > /proc/" + ((Integer) field.get(this.shell.proc)).intValue() + "/oom_adj) &> /dev/null\n");
                this.shell.outputStream.write("(echo -17 > /proc/$$/oom_adj) &> /dev/null\n");
                this.shell.outputStream.flush();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private Shell(String cmd, ShellType shellType, ShellContext shellContext, int shellTimeout) throws IOException, TimeoutException, RootDeniedException {
        RootShell.log("Starting shell: " + cmd);
        RootShell.log("Context: " + shellContext.getValue());
        RootShell.log("Timeout: " + shellTimeout);
        this.shellType = shellType;
        if (shellTimeout <= 0) {
            shellTimeout = this.shellTimeout;
        }
        this.shellTimeout = shellTimeout;
        this.shellContext = shellContext;
        if (this.shellContext == ShellContext.NORMAL) {
            this.proc = Runtime.getRuntime().exec(cmd);
        } else {
            String display = getSuVersion(false);
            String internal = getSuVersion(true);
            if (!isSELinuxEnforcing() || display == null || internal == null || !display.endsWith("SUPERSU") || Integer.valueOf(internal).intValue() < 190) {
                RootShell.log("Su binary --context switch not supported!");
                RootShell.log("Su binary display version: " + display);
                RootShell.log("Su binary internal version: " + internal);
                RootShell.log("SELinuxEnforcing: " + isSELinuxEnforcing());
            } else {
                cmd = cmd + " --context " + this.shellContext.getValue();
            }
            this.proc = Runtime.getRuntime().exec(cmd);
        }
        this.inputStream = new BufferedReader(new InputStreamReader(this.proc.getInputStream(), "UTF-8"));
        this.errorStream = new BufferedReader(new InputStreamReader(this.proc.getErrorStream(), "UTF-8"));
        this.outputStream = new OutputStreamWriter(this.proc.getOutputStream(), "UTF-8");
        Worker worker = new Worker();
        worker.start();
        try {
            worker.join((long) this.shellTimeout);
            if (worker.exit == -911) {
                try {
                    this.proc.destroy();
                } catch (Exception e) {
                }
                closeQuietly(this.inputStream);
                closeQuietly(this.errorStream);
                closeQuietly(this.outputStream);
                throw new TimeoutException(this.error);
            } else if (worker.exit == -42) {
                try {
                    this.proc.destroy();
                } catch (Exception e2) {
                }
                closeQuietly(this.inputStream);
                closeQuietly(this.errorStream);
                closeQuietly(this.outputStream);
                throw new RootDeniedException("Root Access Denied");
            } else {
                Thread si = new Thread(this.input, "Shell Input");
                si.setPriority(5);
                si.start();
                Thread so = new Thread(this.output, "Shell Output");
                so.setPriority(5);
                so.start();
            }
        } catch (InterruptedException e3) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw new TimeoutException();
        }
    }

    public Command add(Command command) throws IOException {
        if (this.close) {
            throw new IllegalStateException("Unable to add commands to a closed shell");
        }
        do {
        } while (this.isCleaning);
        command.resetCommand();
        this.commands.add(command);
        notifyThreads();
        return command;
    }

    public final void useCWD(Context context) throws IOException, TimeoutException, RootDeniedException {
        add(new Command(-1, false, "cd " + context.getApplicationInfo().dataDir));
    }

    private void cleanCommands() {
        this.isCleaning = true;
        int toClean = Math.abs(this.maxCommands - (this.maxCommands / 4));
        RootShell.log("Cleaning up: " + toClean);
        for (int i = 0; i < toClean; i++) {
            this.commands.remove(0);
        }
        this.read = this.commands.size() - 1;
        this.write = this.commands.size() - 1;
        this.isCleaning = false;
    }

    private void closeQuietly(Reader input) {
        if (input != null) {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
    }

    private void closeQuietly(Writer output) {
        if (output != null) {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }

    public void close() throws IOException {
        RootShell.log("Request to close shell!");
        int count = 0;
        while (this.isExecuting) {
            RootShell.log("Waiting on shell to finish executing before closing...");
            count++;
            if (count > 10000) {
                break;
            }
        }
        synchronized (this.commands) {
            this.close = true;
            notifyThreads();
        }
        RootShell.log("Shell Closed!");
        if (this == rootShell) {
            rootShell = null;
        } else if (this == shell) {
            shell = null;
        } else if (this == customShell) {
            customShell = null;
        }
    }

    public static void closeCustomShell() throws IOException {
        RootShell.log("Request to close custom shell!");
        if (customShell != null) {
            customShell.close();
        }
    }

    public static void closeRootShell() throws IOException {
        RootShell.log("Request to close root shell!");
        if (rootShell != null) {
            rootShell.close();
        }
    }

    public static void closeShell() throws IOException {
        RootShell.log("Request to close normal shell!");
        if (shell != null) {
            shell.close();
        }
    }

    public static void closeAll() throws IOException {
        RootShell.log("Request to close all shells!");
        closeShell();
        closeRootShell();
        closeCustomShell();
    }

    public int getCommandQueuePosition(Command cmd) {
        return this.commands.indexOf(cmd);
    }

    public String getCommandQueuePositionString(Command cmd) {
        return "Command is in position " + getCommandQueuePosition(cmd) + " currently executing command at position " + this.write + " and the number of commands is " + this.commands.size();
    }

    public static Shell getOpenShell() {
        if (customShell != null) {
            return customShell;
        }
        if (rootShell != null) {
            return rootShell;
        }
        return shell;
    }

    private synchronized String getSuVersion(boolean internal) {
        String str = null;
        synchronized (this) {
            int idx = internal ? 0 : 1;
            if (suVersion[idx] == null) {
                String version = null;
                try {
                    String line;
                    Process process = Runtime.getRuntime().exec(internal ? "su -V" : "su -v", null);
                    process.waitFor();
                    List<String> stdout = new ArrayList();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while (true) {
                        try {
                            line = reader.readLine();
                            if (line != null) {
                                stdout.add(line);
                            }
                        } catch (IOException e) {
                        }
                        try {
                            break;
                        } catch (IOException e2) {
                        }
                    }
                    reader.close();
                    process.destroy();
                    List<String> ret = stdout;
                    if (ret != null) {
                        for (String line2 : ret) {
                            if (internal) {
                                try {
                                    if (Integer.parseInt(line2) > 0) {
                                        version = line2;
                                        break;
                                    }
                                } catch (NumberFormatException e3) {
                                }
                            } else if (line2.contains(".")) {
                                version = line2;
                                break;
                            }
                        }
                    }
                    suVersion[idx] = version;
                } catch (IOException e4) {
                    e4.printStackTrace();
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }
            }
            str = suVersion[idx];
        }
        return str;
    }

    public static boolean isShellOpen() {
        return shell == null;
    }

    public static boolean isCustomShellOpen() {
        return customShell == null;
    }

    public static boolean isRootShellOpen() {
        return rootShell == null;
    }

    public static boolean isAnyShellOpen() {
        return (shell == null && rootShell == null && customShell == null) ? false : true;
    }

    public synchronized boolean isSELinuxEnforcing() {
        InputStream is;
        if (this.isSELinuxEnforcing == null) {
            Boolean enforcing = null;
            if (VERSION.SDK_INT >= 17) {
                boolean z;
                if (new File("/sys/fs/selinux/enforce").exists()) {
                    try {
                        is = new FileInputStream("/sys/fs/selinux/enforce");
                        if (is.read() == 49) {
                            z = true;
                        } else {
                            z = false;
                        }
                        enforcing = Boolean.valueOf(z);
                        is.close();
                    } catch (Exception e) {
                    } catch (Throwable th) {
                        is.close();
                    }
                }
                if (enforcing == null) {
                    if (VERSION.SDK_INT >= 19) {
                        z = true;
                    } else {
                        z = false;
                    }
                    enforcing = Boolean.valueOf(z);
                }
            }
            if (enforcing == null) {
                enforcing = Boolean.valueOf(false);
            }
            this.isSELinuxEnforcing = enforcing;
        }
        return this.isSELinuxEnforcing.booleanValue();
    }

    protected void notifyThreads() {
        new Thread() {
            public void run() {
                synchronized (Shell.this.commands) {
                    Shell.this.commands.notifyAll();
                }
            }
        }.start();
    }

    public void processErrors(Command command) {
        while (this.errorStream.ready() && command != null) {
            try {
                String line = this.errorStream.readLine();
                if (line != null) {
                    command.output(command.id, line);
                } else {
                    return;
                }
            } catch (Exception e) {
                RootShell.log(e.getMessage(), LogLevel.ERROR, e);
                return;
            }
        }
    }

    public static void runRootCommand(Command command) throws IOException, TimeoutException, RootDeniedException {
        startRootShell().add(command);
    }

    public static void runCommand(Command command) throws IOException, TimeoutException {
        startShell().add(command);
    }

    public static Shell startRootShell() throws IOException, TimeoutException, RootDeniedException {
        return startRootShell(0, 3);
    }

    public static Shell startRootShell(int timeout) throws IOException, TimeoutException, RootDeniedException {
        return startRootShell(timeout, 3);
    }

    public static Shell startRootShell(int timeout, int retry) throws IOException, TimeoutException, RootDeniedException {
        return startRootShell(timeout, defaultContext, retry);
    }

    public static Shell startRootShell(int timeout, ShellContext shellContext, int retry) throws IOException, TimeoutException, RootDeniedException {
        int retries;
        int retries2 = 0;
        if (rootShell == null) {
            RootShell.log("Starting Root Shell!");
            String cmd = "su";
            while (rootShell == null) {
                try {
                    RootShell.log("Trying to open Root Shell, attempt #" + retries2);
                    rootShell = new Shell(cmd, ShellType.ROOT, shellContext, timeout);
                } catch (IOException e) {
                    retries = retries2 + 1;
                    if (retries2 >= retry) {
                        RootShell.log("IOException, could not start shell");
                        throw e;
                    }
                    retries2 = retries;
                } catch (RootDeniedException e2) {
                    retries = retries2 + 1;
                    if (retries2 >= retry) {
                        RootShell.log("RootDeniedException, could not start shell");
                        throw e2;
                    }
                    retries2 = retries;
                } catch (TimeoutException e3) {
                    retries = retries2 + 1;
                    if (retries2 >= retry) {
                        RootShell.log("TimeoutException, could not start shell");
                        throw e3;
                    }
                    retries2 = retries;
                }
            }
        } else if (rootShell.shellContext != shellContext) {
            try {
                RootShell.log("Context is different than open shell, switching context... " + rootShell.shellContext + " VS " + shellContext);
                rootShell.switchRootShellContext(shellContext);
            } catch (IOException e4) {
                retries = 0 + 1;
                if (0 >= retry) {
                    RootShell.log("IOException, could not switch context!");
                    throw e4;
                }
                retries2 = retries;
            } catch (RootDeniedException e22) {
                retries = 0 + 1;
                if (0 >= retry) {
                    RootShell.log("RootDeniedException, could not switch context!");
                    throw e22;
                }
                retries2 = retries;
            } catch (TimeoutException e32) {
                retries = 0 + 1;
                if (0 >= retry) {
                    RootShell.log("TimeoutException, could not switch context!");
                    throw e32;
                }
                retries2 = retries;
            }
        } else {
            RootShell.log("Using Existing Root Shell!");
        }
        return rootShell;
    }

    public static Shell startCustomShell(String shellPath) throws IOException, TimeoutException, RootDeniedException {
        return startCustomShell(shellPath, 0);
    }

    public static Shell startCustomShell(String shellPath, int timeout) throws IOException, TimeoutException, RootDeniedException {
        if (customShell == null) {
            RootShell.log("Starting Custom Shell!");
            customShell = new Shell(shellPath, ShellType.CUSTOM, ShellContext.NORMAL, timeout);
        } else {
            RootShell.log("Using Existing Custom Shell!");
        }
        return customShell;
    }

    public static Shell startShell() throws IOException, TimeoutException {
        return startShell(0);
    }

    public static Shell startShell(int timeout) throws IOException, TimeoutException {
        try {
            if (shell == null) {
                RootShell.log("Starting Shell!");
                shell = new Shell("/system/bin/sh", ShellType.NORMAL, ShellContext.NORMAL, timeout);
            } else {
                RootShell.log("Using Existing Shell!");
            }
            return shell;
        } catch (RootDeniedException e) {
            throw new IOException();
        }
    }

    public Shell switchRootShellContext(ShellContext shellContext) throws IOException, TimeoutException, RootDeniedException {
        if (this.shellType == ShellType.ROOT) {
            try {
                closeRootShell();
            } catch (Exception e) {
                RootShell.log("Problem closing shell while trying to switch context...");
            }
            return startRootShell(this.shellTimeout, shellContext, 3);
        }
        RootShell.log("Can only switch context on a root shell!");
        return this;
    }
}
