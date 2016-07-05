package com.mikepenz.materialize.drawable;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

@SuppressLint({"InlinedApi"})
public class PressedEffectStateListDrawable extends StateListDrawable {
    private int color;
    private int selectionColor;

    public PressedEffectStateListDrawable(Drawable drawable, int color, int selectionColor) {
        drawable = drawable.mutate();
        addState(new int[]{16842913}, drawable);
        addState(new int[0], drawable);
        this.color = color;
        this.selectionColor = selectionColor;
    }

    public PressedEffectStateListDrawable(Drawable drawable, Drawable selectedDrawable, int color, int selectionColor) {
        drawable = drawable.mutate();
        int[] iArr = new int[]{16842913};
        addState(iArr, selectedDrawable.mutate());
        addState(new int[0], drawable);
        this.color = color;
        this.selectionColor = selectionColor;
    }

    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == 16842913) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(this.selectionColor, Mode.SRC_IN);
        } else {
            super.setColorFilter(this.color, Mode.SRC_IN);
        }
        return super.onStateChange(states);
    }

    public boolean isStateful() {
        return true;
    }
}
