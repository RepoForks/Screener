package com.mikepenz.materialdrawer.model.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialize.holder.ColorHolder;
import com.mikepenz.materialize.util.UIUtils;

public class BadgeDrawableBuilder {
    private BadgeStyle mStyle;

    public BadgeDrawableBuilder(BadgeStyle style) {
        this.mStyle = style;
    }

    public StateListDrawable build(Context ctx) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable normal = (GradientDrawable) UIUtils.getCompatDrawable(ctx, this.mStyle.getGradientDrawable());
        GradientDrawable selected = (GradientDrawable) normal.getConstantState().newDrawable().mutate();
        ColorHolder.applyToOrTransparent(this.mStyle.getColor(), ctx, normal);
        if (this.mStyle.getColorPressed() == null) {
            ColorHolder.applyToOrTransparent(this.mStyle.getColor(), ctx, selected);
        } else {
            ColorHolder.applyToOrTransparent(this.mStyle.getColorPressed(), ctx, selected);
        }
        if (this.mStyle.getCorners() != null) {
            normal.setCornerRadius((float) this.mStyle.getCorners().asPixel(ctx));
            selected.setCornerRadius((float) this.mStyle.getCorners().asPixel(ctx));
        }
        stateListDrawable.addState(new int[]{16842919}, selected);
        stateListDrawable.addState(StateSet.WILD_CARD, normal);
        return stateListDrawable;
    }
}
