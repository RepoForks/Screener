package com.mikepenz.aboutlibraries.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import com.mikepenz.aboutlibraries.R;

@SuppressLint({"InlinedApi"})
public class UIUtils {
    public static int getSelectableBackground(Context ctx) {
        if (VERSION.SDK_INT >= 11) {
            TypedValue outValue = new TypedValue();
            ctx.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            return outValue.resourceId;
        }
        outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(16843056, outValue, true);
        return outValue.resourceId;
    }

    public static int getThemeColor(Context ctx, int attr) {
        TypedValue tv = new TypedValue();
        if (ctx.getTheme().resolveAttribute(attr, tv, true)) {
            return tv.data;
        }
        return 0;
    }

    public static int getThemeColorFromAttrOrRes(Context ctx, int attr, int res) {
        int color = getThemeColor(ctx, attr);
        if (color == 0) {
            return ContextCompat.getColor(ctx, res);
        }
        return color;
    }

    @SuppressLint({"NewApi"})
    public static void setBackground(View v, Drawable d) {
        if (VERSION.SDK_INT < 16) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    public static void setBackground(View v, int drawableRes) {
        setBackground(v, getCompatDrawable(v.getContext(), drawableRes));
    }

    public static Drawable getCompatDrawable(Context c, int drawableRes) {
        return ContextCompat.getDrawable(c, drawableRes);
    }

    public static int getThemeAttributeDimensionSize(Context context, int attr) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(new int[]{attr});
            int dimensionPixelSize = a.getDimensionPixelSize(0, 0);
            return dimensionPixelSize;
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
