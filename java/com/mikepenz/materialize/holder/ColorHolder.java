package com.mikepenz.materialize.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import com.mikepenz.materialize.util.UIUtils;

public class ColorHolder {
    private int mColorInt = 0;
    private int mColorRes = -1;

    public int getColorInt() {
        return this.mColorInt;
    }

    public void setColorInt(int mColorInt) {
        this.mColorInt = mColorInt;
    }

    public int getColorRes() {
        return this.mColorRes;
    }

    public void setColorRes(int mColorRes) {
        this.mColorRes = mColorRes;
    }

    public static ColorHolder fromColorRes(@ColorRes int colorRes) {
        ColorHolder colorHolder = new ColorHolder();
        colorHolder.mColorRes = colorRes;
        return colorHolder;
    }

    public static ColorHolder fromColor(@ColorInt int colorInt) {
        ColorHolder colorHolder = new ColorHolder();
        colorHolder.mColorInt = colorInt;
        return colorHolder;
    }

    public void applyTo(Context ctx, GradientDrawable drawable) {
        if (this.mColorInt != 0) {
            drawable.setColor(this.mColorInt);
        } else if (this.mColorRes != -1) {
            drawable.setColor(ContextCompat.getColor(ctx, this.mColorRes));
        }
    }

    public void applyToBackground(View view) {
        if (this.mColorInt != 0) {
            view.setBackgroundColor(this.mColorInt);
        } else if (this.mColorRes != -1) {
            view.setBackgroundResource(this.mColorRes);
        }
    }

    public void applyToOr(TextView textView, ColorStateList colorDefault) {
        if (this.mColorInt != 0) {
            textView.setTextColor(this.mColorInt);
        } else if (this.mColorRes != -1) {
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), this.mColorRes));
        } else if (colorDefault != null) {
            textView.setTextColor(colorDefault);
        }
    }

    public int color(Context ctx, @AttrRes int colorStyle, @ColorRes int colorDefaultRes) {
        int color = color(ctx);
        if (color == 0) {
            return UIUtils.getThemeColorFromAttrOrRes(ctx, colorStyle, colorDefaultRes);
        }
        return color;
    }

    public int color(Context ctx) {
        if (this.mColorInt == 0 && this.mColorRes != -1) {
            this.mColorInt = ContextCompat.getColor(ctx, this.mColorRes);
        }
        return this.mColorInt;
    }

    public static int color(ColorHolder colorHolder, Context ctx, @AttrRes int colorStyle, @ColorRes int colorDefault) {
        if (colorHolder == null) {
            return UIUtils.getThemeColorFromAttrOrRes(ctx, colorStyle, colorDefault);
        }
        return colorHolder.color(ctx, colorStyle, colorDefault);
    }

    public static int color(ColorHolder colorHolder, Context ctx) {
        if (colorHolder == null) {
            return 0;
        }
        return colorHolder.color(ctx);
    }

    public static void applyToOr(ColorHolder colorHolder, TextView textView, ColorStateList colorDefault) {
        if (colorHolder != null && textView != null) {
            colorHolder.applyToOr(textView, colorDefault);
        } else if (textView != null) {
            textView.setTextColor(colorDefault);
        }
    }

    public static void applyToOrTransparent(ColorHolder colorHolder, Context ctx, GradientDrawable gradientDrawable) {
        if (colorHolder != null && gradientDrawable != null) {
            colorHolder.applyTo(ctx, gradientDrawable);
        } else if (gradientDrawable != null) {
            gradientDrawable.setColor(0);
        }
    }
}
