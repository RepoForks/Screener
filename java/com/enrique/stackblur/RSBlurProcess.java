package com.enrique.stackblur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Allocation.MipmapControl;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;

class RSBlurProcess implements BlurProcess {
    private final RenderScript _rs = RenderScript.create(this.context);
    private final Context context;

    public RSBlurProcess(Context context) {
        this.context = context.getApplicationContext();
    }

    public Bitmap blur(Bitmap original, float radius) {
        int i;
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap blurred = original.copy(Config.ARGB_8888, true);
        ScriptC_blur blurScript = new ScriptC_blur(this._rs, this.context.getResources(), R.raw.blur);
        Allocation inAllocation = Allocation.createFromBitmap(this._rs, blurred, MipmapControl.MIPMAP_NONE, 1);
        blurScript.set_gIn(inAllocation);
        blurScript.set_width((long) width);
        blurScript.set_height((long) height);
        blurScript.set_radius((long) ((int) radius));
        int[] row_indices = new int[height];
        for (i = 0; i < height; i++) {
            row_indices[i] = i;
        }
        Allocation rows = Allocation.createSized(this._rs, Element.U32(this._rs), height, 1);
        rows.copyFrom(row_indices);
        row_indices = new int[width];
        for (i = 0; i < width; i++) {
            row_indices[i] = i;
        }
        Allocation columns = Allocation.createSized(this._rs, Element.U32(this._rs), width, 1);
        columns.copyFrom(row_indices);
        blurScript.forEach_blur_h(rows);
        blurScript.forEach_blur_v(columns);
        inAllocation.copyTo(blurred);
        return blurred;
    }
}
