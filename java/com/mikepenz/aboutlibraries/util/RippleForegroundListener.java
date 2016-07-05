package com.mikepenz.aboutlibraries.util;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import me.zhanghai.android.materialprogressbar.R;

public class RippleForegroundListener implements OnTouchListener {
    private int rippleViewId = -1;

    public RippleForegroundListener(int rippleViewId) {
        this.rippleViewId = rippleViewId;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX() + ((float) v.getLeft());
        float y = event.getY() + ((float) v.getTop());
        View rippleView = findRippleView(v);
        if (rippleView != null) {
            if (VERSION.SDK_INT >= 21) {
                rippleView.drawableHotspotChanged(x, y);
            }
            switch (event.getActionMasked()) {
                case R.styleable.View_android_theme /*0*/:
                    rippleView.setPressed(true);
                    break;
                case R.styleable.View_android_focusable /*1*/:
                case R.styleable.View_paddingEnd /*3*/:
                    rippleView.setPressed(false);
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public View findRippleView(View view) {
        if (view.getId() == this.rippleViewId) {
            return view;
        }
        if (view.getParent() instanceof View) {
            return findRippleView((View) view.getParent());
        }
        return null;
    }
}
