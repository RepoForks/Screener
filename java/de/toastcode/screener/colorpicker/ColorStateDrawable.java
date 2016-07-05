package de.toastcode.screener.colorpicker;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

public class ColorStateDrawable extends LayerDrawable {
    private static final float PRESSED_STATE_MULTIPLIER = 0.7f;
    private int mColor;

    public ColorStateDrawable(Drawable[] layers, int color) {
        super(layers);
        this.mColor = color;
    }

    protected boolean onStateChange(int[] states) {
        boolean pressedOrFocused = false;
        for (int state : states) {
            if (state == 16842919 || state == 16842908) {
                pressedOrFocused = true;
                break;
            }
        }
        if (pressedOrFocused) {
            super.setColorFilter(getPressedColor(this.mColor), Mode.SRC_ATOP);
        } else {
            super.setColorFilter(this.mColor, Mode.SRC_ATOP);
        }
        return super.onStateChange(states);
    }

    private int getPressedColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * PRESSED_STATE_MULTIPLIER;
        return Color.HSVToColor(hsv);
    }

    public boolean isStateful() {
        return true;
    }
}
