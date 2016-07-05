package com.enrique.stackblur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.v8.renderscript.RSRuntimeException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StackBlurManager {
    static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);
    static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
    private static volatile boolean hasRS = true;
    private final BlurProcess _blurProcess = new JavaBlurProcess();
    private final Bitmap _image;
    private Bitmap _result;

    public StackBlurManager(Bitmap image) {
        this._image = image;
    }

    public Bitmap process(int radius) {
        this._result = this._blurProcess.blur(this._image, (float) radius);
        return this._result;
    }

    public Bitmap returnBlurredImage() {
        return this._result;
    }

    public void saveIntoFile(String path) {
        try {
            this._result.compress(CompressFormat.PNG, 90, new FileOutputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImage() {
        return this._image;
    }

    public Bitmap processNatively(int radius) {
        this._result = new NativeBlurProcess().blur(this._image, (float) radius);
        return this._result;
    }

    public Bitmap processRenderScript(Context context, float radius) {
        BlurProcess blurProcess;
        if (hasRS) {
            try {
                blurProcess = new RSBlurProcess(context);
            } catch (RSRuntimeException e) {
                blurProcess = new NativeBlurProcess();
                hasRS = false;
            }
        } else {
            blurProcess = new NativeBlurProcess();
        }
        this._result = blurProcess.blur(this._image, radius);
        return this._result;
    }
}
