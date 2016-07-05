package com.mikepenz.materialize.util;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
    private View contentView;
    private View decorView;
    private float initialDpDiff = -1.0f;
    OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            Rect r = new Rect();
            KeyboardUtil.this.decorView.getWindowVisibleDisplayFrame(r);
            float heightDiffDp = UIUtils.convertPixelsToDp((float) (KeyboardUtil.this.decorView.getRootView().getHeight() - (r.bottom - r.top)), KeyboardUtil.this.decorView.getContext());
            if (KeyboardUtil.this.initialDpDiff == -1.0f) {
                KeyboardUtil.this.initialDpDiff = heightDiffDp;
            }
            if (heightDiffDp - KeyboardUtil.this.initialDpDiff > 100.0f) {
                if (KeyboardUtil.this.contentView.getPaddingBottom() == 0) {
                    KeyboardUtil.this.contentView.setPadding(0, 0, 0, (int) UIUtils.convertDpToPixel(heightDiffDp - KeyboardUtil.this.initialDpDiff, KeyboardUtil.this.decorView.getContext()));
                }
            } else if (KeyboardUtil.this.contentView.getPaddingBottom() != 0) {
                KeyboardUtil.this.contentView.setPadding(0, 0, 0, 0);
            }
        }
    };

    public KeyboardUtil(Activity act, View contentView) {
        this.decorView = act.getWindow().getDecorView();
        this.contentView = contentView;
        if (VERSION.SDK_INT >= 19) {
            this.decorView.getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
    }

    public void enable() {
        if (VERSION.SDK_INT >= 19) {
            this.decorView.getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
    }

    public void disable() {
        if (VERSION.SDK_INT >= 19) {
            this.decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
    }

    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            ((InputMethodManager) act.getSystemService("input_method")).hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
