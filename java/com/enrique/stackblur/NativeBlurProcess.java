package com.enrique.stackblur;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class NativeBlurProcess implements BlurProcess {

    private static class NativeTask implements Callable<Void> {
        private final Bitmap _bitmapOut;
        private final int _coreIndex;
        private final int _radius;
        private final int _round;
        private final int _totalCores;

        public NativeTask(Bitmap bitmapOut, int radius, int totalCores, int coreIndex, int round) {
            this._bitmapOut = bitmapOut;
            this._radius = radius;
            this._totalCores = totalCores;
            this._coreIndex = coreIndex;
            this._round = round;
        }

        public Void call() throws Exception {
            NativeBlurProcess.functionToBlur(this._bitmapOut, this._radius, this._totalCores, this._coreIndex, this._round);
            return null;
        }
    }

    private static native void functionToBlur(Bitmap bitmap, int i, int i2, int i3, int i4);

    NativeBlurProcess() {
    }

    static {
        System.loadLibrary("blur");
    }

    public Bitmap blur(Bitmap original, float radius) {
        Bitmap bitmapOut = original.copy(Config.ARGB_8888, true);
        int cores = StackBlurManager.EXECUTOR_THREADS;
        ArrayList<NativeTask> horizontal = new ArrayList(cores);
        ArrayList<NativeTask> vertical = new ArrayList(cores);
        for (int i = 0; i < cores; i++) {
            horizontal.add(new NativeTask(bitmapOut, (int) radius, cores, i, 1));
            vertical.add(new NativeTask(bitmapOut, (int) radius, cores, i, 2));
        }
        try {
            StackBlurManager.EXECUTOR.invokeAll(horizontal);
            try {
                StackBlurManager.EXECUTOR.invokeAll(vertical);
            } catch (InterruptedException e) {
            }
        } catch (InterruptedException e2) {
        }
        return bitmapOut;
    }
}
