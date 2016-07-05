package com.enrique.stackblur;

import android.content.res.Resources;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.FieldPacker;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;
import android.support.v8.renderscript.Script.LaunchOptions;
import android.support.v8.renderscript.ScriptC;

public class ScriptC_blur extends ScriptC {
    private static final String __rs_resource_name = "blur";
    private static final int mExportForEachIdx_blur_h = 2;
    private static final int mExportForEachIdx_blur_v = 1;
    private static final int mExportVarIdx_gIn = 0;
    private static final int mExportVarIdx_height = 2;
    private static final int mExportVarIdx_radius = 3;
    private static final int mExportVarIdx_width = 1;
    private Element __ALLOCATION;
    private Element __U32;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_U32;
    private Allocation mExportVar_gIn;
    private long mExportVar_height;
    private long mExportVar_radius;
    private long mExportVar_width;

    public ScriptC_blur(RenderScript rs) {
        this(rs, rs.getApplicationContext().getResources(), rs.getApplicationContext().getResources().getIdentifier(__rs_resource_name, "raw", rs.getApplicationContext().getPackageName()));
    }

    public ScriptC_blur(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        this.__ALLOCATION = Element.ALLOCATION(rs);
        this.__U32 = Element.U32(rs);
    }

    public synchronized void set_gIn(Allocation v) {
        setVar(mExportVarIdx_gIn, v);
        this.mExportVar_gIn = v;
    }

    public Allocation get_gIn() {
        return this.mExportVar_gIn;
    }

    public FieldID getFieldID_gIn() {
        return createFieldID(mExportVarIdx_gIn, null);
    }

    public synchronized void set_width(long v) {
        if (this.__rs_fp_U32 != null) {
            this.__rs_fp_U32.reset();
        } else {
            this.__rs_fp_U32 = new FieldPacker(4);
        }
        this.__rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_width, this.__rs_fp_U32);
        this.mExportVar_width = v;
    }

    public long get_width() {
        return this.mExportVar_width;
    }

    public FieldID getFieldID_width() {
        return createFieldID(mExportVarIdx_width, null);
    }

    public synchronized void set_height(long v) {
        if (this.__rs_fp_U32 != null) {
            this.__rs_fp_U32.reset();
        } else {
            this.__rs_fp_U32 = new FieldPacker(4);
        }
        this.__rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_height, this.__rs_fp_U32);
        this.mExportVar_height = v;
    }

    public long get_height() {
        return this.mExportVar_height;
    }

    public FieldID getFieldID_height() {
        return createFieldID(mExportVarIdx_height, null);
    }

    public synchronized void set_radius(long v) {
        if (this.__rs_fp_U32 != null) {
            this.__rs_fp_U32.reset();
        } else {
            this.__rs_fp_U32 = new FieldPacker(4);
        }
        this.__rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_radius, this.__rs_fp_U32);
        this.mExportVar_radius = v;
    }

    public long get_radius() {
        return this.mExportVar_radius;
    }

    public FieldID getFieldID_radius() {
        return createFieldID(mExportVarIdx_radius, null);
    }

    public KernelID getKernelID_blur_v() {
        return createKernelID(mExportVarIdx_width, 33, null, null);
    }

    public void forEach_blur_v(Allocation ain) {
        forEach_blur_v(ain, null);
    }

    public void forEach_blur_v(Allocation ain, LaunchOptions sc) {
        if (ain.getType().getElement().isCompatible(this.__U32)) {
            forEach(mExportVarIdx_width, ain, null, null, sc);
            return;
        }
        throw new RSRuntimeException("Type mismatch with U32!");
    }

    public KernelID getKernelID_blur_h() {
        return createKernelID(mExportVarIdx_height, 33, null, null);
    }

    public void forEach_blur_h(Allocation ain) {
        forEach_blur_h(ain, null);
    }

    public void forEach_blur_h(Allocation ain, LaunchOptions sc) {
        if (ain.getType().getElement().isCompatible(this.__U32)) {
            forEach(mExportVarIdx_height, ain, null, null, sc);
            return;
        }
        throw new RSRuntimeException("Type mismatch with U32!");
    }
}
