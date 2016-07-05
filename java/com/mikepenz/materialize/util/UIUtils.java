package com.mikepenz.materialize.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import com.mikepenz.materialize.R;

@SuppressLint({"InlinedApi"})
public class UIUtils {
    public static int getThemeColor(Context ctx, @AttrRes int attr) {
        TypedValue tv = new TypedValue();
        if (ctx.getTheme().resolveAttribute(attr, tv, true)) {
            return tv.data;
        }
        return 0;
    }

    public static int getThemeColorFromAttrOrRes(Context ctx, @AttrRes int attr, @ColorRes int res) {
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

    public static void setBackground(View v, @DrawableRes int drawableRes) {
        setBackground(v, getCompatDrawable(v.getContext(), drawableRes));
    }

    public static Drawable getCompatDrawable(Context c, @DrawableRes int drawableRes) {
        try {
            if (VERSION.SDK_INT < 21) {
                return c.getResources().getDrawable(drawableRes);
            }
            return c.getResources().getDrawable(drawableRes, c.getTheme());
        } catch (Exception e) {
            return null;
        }
    }

    public static int getThemeAttributeDimensionSize(Context context, @AttrRes int attr) {
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

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier(context.getResources().getConfiguration().orientation == 1 ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = getThemeAttributeDimensionSize(context, R.attr.actionBarSize);
        if (actionBarHeight == 0) {
            return context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
        }
        return actionBarHeight;
    }

    public static int getStatusBarHeight(Context context) {
        return getStatusBarHeight(context, false);
    }

    public static int getStatusBarHeight(Context context, boolean force) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        int dimenResult = context.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding);
        if (dimenResult != 0 || force) {
            return result != 0 ? result : dimenResult;
        } else {
            return 0;
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static void setTranslucentStatusFlag(Activity activity, boolean on) {
        if (VERSION.SDK_INT >= 19) {
            setFlag(activity, 67108864, on);
        }
    }

    public static void setTranslucentNavigationFlag(Activity activity, boolean on) {
        if (VERSION.SDK_INT >= 19) {
            setFlag(activity, 134217728, on);
        }
    }

    public static void setFlag(Activity activity, int bits, boolean on) {
        Window win = activity.getWindow();
        LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= bits ^ -1;
        }
        win.setAttributes(winParams);
    }

    public static StateListDrawable getIconStateList(Drawable icon, Drawable selectedIcon) {
        StateListDrawable iconStateListDrawable = new StateListDrawable();
        iconStateListDrawable.addState(new int[]{16842913}, selectedIcon);
        iconStateListDrawable.addState(new int[0], icon);
        return iconStateListDrawable;
    }

    public static StateListDrawable getSelectableBackground(Context ctx, int selected_color, boolean animate) {
        StateListDrawable states = new StateListDrawable();
        int[] iArr = new int[]{16842913};
        states.addState(iArr, new ColorDrawable(selected_color));
        states.addState(new int[0], getSelectableBackground(ctx));
        if (animate && VERSION.SDK_INT >= 11) {
            int duration = ctx.getResources().getInteger(17694720);
            states.setEnterFadeDuration(duration);
            states.setExitFadeDuration(duration);
        }
        return states;
    }

    public static StateListDrawable getSelectablePressedBackground(Context ctx, int selected_color, int pressed_alpha, boolean animate) {
        StateListDrawable states = getSelectableBackground(ctx, selected_color, animate);
        int[] iArr = new int[]{16842919};
        states.addState(iArr, new ColorDrawable(adjustAlpha(selected_color, pressed_alpha)));
        return states;
    }

    public static int adjustAlpha(int color, int alpha) {
        return (alpha << 24) | (16777215 & color);
    }

    public static int getSelectableBackgroundRes(Context ctx) {
        if (VERSION.SDK_INT >= 11) {
            TypedValue outValue = new TypedValue();
            ctx.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            return outValue.resourceId;
        }
        outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(16843056, outValue, true);
        return outValue.resourceId;
    }

    public static Drawable getSelectableBackground(Context ctx) {
        int selectableBackgroundRes = getSelectableBackgroundRes(ctx);
        if (VERSION.SDK_INT >= 11) {
            return ContextCompat.getDrawable(ctx, selectableBackgroundRes);
        }
        return getCompatDrawable(ctx, selectableBackgroundRes);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void setAlpha(View v, float value) {
        if (VERSION.SDK_INT < 11) {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            v.startAnimation(alpha);
            return;
        }
        v.setAlpha(value);
    }
}
