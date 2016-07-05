package me.zhanghai.android.materialprogressbar.internal;

import android.content.Context;
import android.content.res.TypedArray;

public class ThemeUtils {
    private ThemeUtils() {
    }

    public static int getColorFromAttrRes(int attr, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attr});
        try {
            int color = a.getColor(0, 0);
            return color;
        } finally {
            a.recycle();
        }
    }

    public static float getFloatFromAttrRes(int attrRes, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrRes});
        try {
            float f = a.getFloat(0, 0.0f);
            return f;
        } finally {
            a.recycle();
        }
    }
}
