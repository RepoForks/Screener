package com.mikepenz.fastadapter.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import com.mikepenz.fastadapter.R;

public class FastAdapterUIUtils {
    public static StateListDrawable getSelectableBackground(Context ctx, @ColorInt int selected_color, boolean animate) {
        StateListDrawable states = new StateListDrawable();
        int[] iArr = new int[]{16842913};
        states.addState(iArr, new ColorDrawable(selected_color));
        states.addState(new int[0], ContextCompat.getDrawable(ctx, getSelectableBackground(ctx)));
        if (animate && VERSION.SDK_INT >= 11) {
            int duration = ctx.getResources().getInteger(17694720);
            states.setEnterFadeDuration(duration);
            states.setExitFadeDuration(duration);
        }
        return states;
    }

    public static StateListDrawable getSelectablePressedBackground(Context ctx, @ColorInt int selected_color, int pressed_alpha, boolean animate) {
        StateListDrawable states = getSelectableBackground(ctx, selected_color, animate);
        int[] iArr = new int[]{16842919};
        states.addState(iArr, new ColorDrawable(adjustAlpha(selected_color, pressed_alpha)));
        return states;
    }

    public static int adjustAlpha(@ColorInt int color, int alpha) {
        return (alpha << 24) | (16777215 & color);
    }

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
}
