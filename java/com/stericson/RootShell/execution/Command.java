package com.stericson.RootShell.execution;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.stericson.RootShell.RootShell;
import java.io.IOException;

public class Command {
    String[] command;
    protected Context context;
    boolean executing;
    ExecutionMonitor executionMonitor;
    int exitCode;
    boolean finished;
    boolean handlerEnabled;
    int id;
    protected boolean javaCommand;
    Handler mHandler;
    boolean terminated;
    int timeout;
    public int totalOutput;
    public int totalOutputProcessed;

    private class CommandHandler extends Handler {
        public static final String ACTION = "action";
        public static final int COMMAND_COMPLETED = 2;
        public static final int COMMAND_OUTPUT = 1;
        public static final int COMMAND_TERMINATED = 3;
        public static final String TEXT = "text";

        private CommandHandler() {
        }

        public final void handleMessage(Message msg) {
            int action = msg.getData().getInt(ACTION);
            String text = msg.getData().getString(TEXT);
            switch (action) {
                case COMMAND_OUTPUT /*1*/:
                    Command.this.commandOutput(Command.this.id, text);
                    return;
                case COMMAND_COMPLETED /*2*/:
                    Command.this.commandCompleted(Command.this.id, Command.this.exitCode);
                    return;
                case COMMAND_TERMINATED /*3*/:
                    Command.this.commandTerminated(Command.this.id, text);
                    return;
                default:
                    return;
            }
        }
    }

    private class ExecutionMonitor extends Thread {
        private ExecutionMonitor() {
        }

        public void run() {
            if (Command.this.timeout > 0) {
                while (!Command.this.finished) {
                    synchronized (Command.this) {
                        try {
                            Command.this.wait((long) Command.this.timeout);
                        } catch (InterruptedException e) {
                        }
                    }
                    if (!Command.this.finished) {
                        RootShell.log("Timeout Exception has occurred.");
                        Command.this.terminate("Timeout Exception");
                    }
                }
            }
        }
    }

    public Command(int id, String... command) {
        this.javaCommand = false;
        this.context = null;
        this.totalOutput = 0;
        this.totalOutputProcessed = 0;
        this.executionMonitor = null;
        this.mHandler = null;
        this.executing = false;
        this.command = new String[0];
        this.finished = false;
        this.terminated = false;
        this.handlerEnabled = true;
        this.exitCode = -1;
        this.id = 0;
        this.timeout = RootShell.defaultCommandTimeout;
        this.command = command;
        this.id = id;
        createHandler(RootShell.handlerEnabled);
    }

    public Command(int id, boolean handlerEnabled, String... command) {
        this.javaCommand = false;
        this.context = null;
        this.totalOutput = 0;
        this.totalOutputProcessed = 0;
        this.executionMonitor = null;
        this.mHandler = null;
        this.executing = false;
        this.command = new String[0];
        this.finished = false;
        this.terminated = false;
        this.handlerEnabled = true;
        this.exitCode = -1;
        this.id = 0;
        this.timeout = RootShell.defaultCommandTimeout;
        this.command = command;
        this.id = id;
        createHandler(handlerEnabled);
    }

    public Command(int id, int timeout, String... command) {
        this.javaCommand = false;
        this.context = null;
        this.totalOutput = 0;
        this.totalOutputProcessed = 0;
        this.executionMonitor = null;
        this.mHandler = null;
        this.executing = false;
        this.command = new String[0];
        this.finished = false;
        this.terminated = false;
        this.handlerEnabled = true;
        this.exitCode = -1;
        this.id = 0;
        this.timeout = RootShell.defaultCommandTimeout;
        this.command = command;
        this.id = id;
        this.timeout = timeout;
        createHandler(RootShell.handlerEnabled);
    }

    public void commandOutput(int id, String line) {
        RootShell.log("Command", "ID: " + id + ", " + line);
        this.totalOutputProcessed++;
    }

    public void commandTerminated(int id, String reason) {
    }

    public void commandCompleted(int id, int exitcode) {
    }

    protected final void commandFinished() {
        if (!this.terminated) {
            synchronized (this) {
                if (this.mHandler == null || !this.handlerEnabled) {
                    commandCompleted(this.id, this.exitCode);
                } else {
                    Message msg = this.mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommandHandler.ACTION, 2);
                    msg.setData(bundle);
                    this.mHandler.sendMessage(msg);
                }
                RootShell.log("Command " + this.id + " finished.");
                finishCommand();
            }
        }
    }

    private void createHandler(boolean handlerEnabled) {
        this.handlerEnabled = handlerEnabled;
        if (Looper.myLooper() == null || !handlerEnabled) {
            RootShell.log("CommandHandler not created");
            return;
        }
        RootShell.log("CommandHandler created");
        this.mHandler = new CommandHandler();
    }

    public final void finish() {
        RootShell.log("Command finished at users request!");
        commandFinished();
    }

    protected final void finishCommand() {
        this.executing = false;
        this.finished = true;
        notifyAll();
    }

    public final String getCommand() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.command.length; i++) {
            if (i > 0) {
                sb.append('\n');
            }
            sb.append(this.command[i]);
        }
        return sb.toString();
    }

    public final boolean isExecuting() {
        return this.executing;
    }

    public final boolean isHandlerEnabled() {
        return this.handlerEnabled;
    }

    public final boolean isFinished() {
        return this.finished;
    }

    public final int getExitCode() {
        return this.exitCode;
    }

    protected final void setExitCode(int code) {
        synchronized (this) {
            this.exitCode = code;
        }
    }

    protected final void startExecution() {
        this.executionMonitor = new ExecutionMonitor();
        this.executionMonitor.setPriority(1);
        this.executionMonitor.start();
        this.executing = true;
    }

    public final void terminate() {
        RootShell.log("Terminating command at users request!");
        terminated("Terminated at users request!");
    }

    protected final void terminate(String reason) {
        try {
            Shell.closeAll();
            RootShell.log("Terminating all shells.");
            terminated(reason);
        } catch (IOException e) {
        }
    }

    protected final void terminated(String reason) {
        synchronized (this) {
            if (this.mHandler == null || !this.handlerEnabled) {
                commandTerminated(this.id, reason);
            } else {
                Message msg = this.mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt(CommandHandler.ACTION, 3);
                bundle.putString(CommandHandler.TEXT, reason);
                msg.setData(bundle);
                this.mHandler.sendMessage(msg);
            }
            RootShell.log("Command " + this.id + " did not finish because it was terminated. Termination reason: " + reason);
            setExitCode(-1);
            this.terminated = true;
            finishCommand();
        }
    }

    protected final void output(int id, String line) {
        this.totalOutput++;
        if (this.mHandler == null || !this.handlerEnabled) {
            commandOutput(id, line);
            return;
        }
        Message msg = this.mHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putInt(CommandHandler.ACTION, 1);
        bundle.putString(CommandHandler.TEXT, line);
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    public final void resetCommand() {
        this.finished = false;
        this.totalOutput = 0;
        this.totalOutputProcessed = 0;
        this.executing = false;
        this.terminated = false;
        this.exitCode = -1;
    }
}
