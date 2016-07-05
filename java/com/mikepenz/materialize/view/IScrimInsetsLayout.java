package com.mikepenz.materialize.view;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

public interface IScrimInsetsLayout {
    Drawable getInsetForeground();

    OnInsetsCallback getOnInsetsCallback();

    ViewGroup getView();

    boolean isSystemUIVisible();

    boolean isTintNavigationBar();

    boolean isTintStatusBar();

    void setFitsSystemWindows(boolean z);

    void setInsetForeground(int i);

    void setInsetForeground(Drawable drawable);

    void setOnInsetsCallback(OnInsetsCallback onInsetsCallback);

    void setSystemUIVisible(boolean z);

    void setTintNavigationBar(boolean z);

    void setTintStatusBar(boolean z);
}
