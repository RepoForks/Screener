package com.stericson.RootTools.internal;

import android.content.Context;
import android.util.Log;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootTools.RootTools;
import java.io.IOException;

public class Runner extends Thread {
    private static final String LOG_TAG = "RootTools::Runner";
    String binaryName;
    Context context;
    String parameter;

    public Runner(Context context, String binaryName, String parameter) {
        this.context = context;
        this.binaryName = binaryName;
        this.parameter = parameter;
    }

    public void run() {
        String privateFilesPath = null;
        try {
            privateFilesPath = this.context.getFilesDir().getCanonicalPath();
        } catch (IOException e) {
            if (RootTools.debugMode) {
                Log.e(LOG_TAG, "Problem occured while trying to locate private files directory!");
            }
            e.printStackTrace();
        }
        if (privateFilesPath != null) {
            try {
                Command command = new Command(0, false, privateFilesPath + "/" + this.binaryName + " " + this.parameter);
                Shell.startRootShell().add(command);
                commandWait(command);
            } catch (Exception e2) {
            }
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
