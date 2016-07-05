package com.mikepenz.materialize;

import android.app.Activity;
import android.view.ViewGroup;
import com.mikepenz.materialize.util.KeyboardUtil;
import com.mikepenz.materialize.view.IScrimInsetsLayout;

public class Materialize {
    private final MaterializeBuilder mBuilder;
    private KeyboardUtil mKeyboardUtil = null;

    protected Materialize(MaterializeBuilder materializeBuilder) {
        this.mBuilder = materializeBuilder;
    }

    public void setFullscreen(boolean fullscreen) {
        boolean z = true;
        if (this.mBuilder.mScrimInsetsLayout != null) {
            boolean z2;
            IScrimInsetsLayout iScrimInsetsLayout = this.mBuilder.mScrimInsetsLayout;
            if (fullscreen) {
                z2 = false;
            } else {
                z2 = true;
            }
            iScrimInsetsLayout.setTintStatusBar(z2);
            IScrimInsetsLayout iScrimInsetsLayout2 = this.mBuilder.mScrimInsetsLayout;
            if (fullscreen) {
                z = false;
            }
            iScrimInsetsLayout2.setTintNavigationBar(z);
        }
    }

    public void setTintStatusBar(boolean tintStatusBar) {
        if (this.mBuilder.mScrimInsetsLayout != null) {
            this.mBuilder.mScrimInsetsLayout.setTintStatusBar(tintStatusBar);
        }
    }

    public void setTintNavigationBar(boolean tintNavigationBar) {
        if (this.mBuilder.mScrimInsetsLayout != null) {
            this.mBuilder.mScrimInsetsLayout.setTintNavigationBar(tintNavigationBar);
        }
    }

    public void setStatusBarColor(int statusBarColor) {
        if (this.mBuilder.mScrimInsetsLayout != null) {
            this.mBuilder.mScrimInsetsLayout.setInsetForeground(statusBarColor);
            this.mBuilder.mScrimInsetsLayout.getView().invalidate();
        }
    }

    public IScrimInsetsLayout getScrimInsetsFrameLayout() {
        return this.mBuilder.mScrimInsetsLayout;
    }

    public ViewGroup getContent() {
        return this.mBuilder.mContentRoot;
    }

    public void keyboardSupportEnabled(Activity activity, boolean enable) {
        if (getContent() != null && getContent().getChildCount() > 0) {
            if (this.mKeyboardUtil == null) {
                this.mKeyboardUtil = new KeyboardUtil(activity, getContent().getChildAt(0));
                this.mKeyboardUtil.disable();
            }
            if (enable) {
                this.mKeyboardUtil.enable();
            } else {
                this.mKeyboardUtil.disable();
            }
        }
    }
}
