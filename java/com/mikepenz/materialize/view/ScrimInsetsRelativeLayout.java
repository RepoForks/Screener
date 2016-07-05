package com.mikepenz.materialize.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.mikepenz.materialize.R;

public class ScrimInsetsRelativeLayout extends RelativeLayout implements IScrimInsetsLayout {
    private Drawable mInsetForeground;
    private Rect mInsets;
    private OnInsetsCallback mOnInsetsCallback;
    private boolean mSystemUIVisible;
    private Rect mTempRect;
    private boolean mTintNavigationBar;
    private boolean mTintStatusBar;

    public ScrimInsetsRelativeLayout(Context context) {
        this(context, null);
    }

    public ScrimInsetsRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrimInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTempRect = new Rect();
        this.mTintStatusBar = true;
        this.mTintNavigationBar = true;
        this.mSystemUIVisible = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrimInsetsRelativeLayout, defStyleAttr, R.style.Widget_Materialize_ScrimInsetsRelativeLayout);
        this.mInsetForeground = a.getDrawable(R.styleable.ScrimInsetsRelativeLayout_sirl_insetForeground);
        a.recycle();
        setWillNotDraw(true);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if (ScrimInsetsRelativeLayout.this.mInsets == null) {
                    ScrimInsetsRelativeLayout.this.mInsets = new Rect();
                }
                ScrimInsetsRelativeLayout.this.mInsets.set(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
                ScrimInsetsRelativeLayout.this.setWillNotDraw(ScrimInsetsRelativeLayout.this.mInsetForeground == null);
                ViewCompat.postInvalidateOnAnimation(ScrimInsetsRelativeLayout.this);
                if (ScrimInsetsRelativeLayout.this.mOnInsetsCallback != null) {
                    ScrimInsetsRelativeLayout.this.mOnInsetsCallback.onInsetsChanged(insets);
                }
                return insets.consumeSystemWindowInsets();
            }
        });
    }

    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.mInsets != null && this.mInsetForeground != null) {
            int sc = canvas.save();
            canvas.translate((float) getScrollX(), (float) getScrollY());
            if (!this.mSystemUIVisible) {
                this.mInsets.top = 0;
                this.mInsets.right = 0;
                this.mInsets.bottom = 0;
                this.mInsets.left = 0;
            }
            if (this.mTintStatusBar) {
                this.mTempRect.set(0, 0, width, this.mInsets.top);
                this.mInsetForeground.setBounds(this.mTempRect);
                this.mInsetForeground.draw(canvas);
            }
            if (this.mTintNavigationBar) {
                this.mTempRect.set(0, height - this.mInsets.bottom, width, height);
                this.mInsetForeground.setBounds(this.mTempRect);
                this.mInsetForeground.draw(canvas);
            }
            this.mTempRect.set(0, this.mInsets.top, this.mInsets.left, height - this.mInsets.bottom);
            this.mInsetForeground.setBounds(this.mTempRect);
            this.mInsetForeground.draw(canvas);
            this.mTempRect.set(width - this.mInsets.right, this.mInsets.top, width, height - this.mInsets.bottom);
            this.mInsetForeground.setBounds(this.mTempRect);
            this.mInsetForeground.draw(canvas);
            canvas.restoreToCount(sc);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mInsetForeground != null) {
            this.mInsetForeground.setCallback(this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mInsetForeground != null) {
            this.mInsetForeground.setCallback(null);
        }
    }

    public ViewGroup getView() {
        return this;
    }

    public Drawable getInsetForeground() {
        return this.mInsetForeground;
    }

    public void setInsetForeground(Drawable mInsetForeground) {
        this.mInsetForeground = mInsetForeground;
    }

    public void setInsetForeground(int mInsetForegroundColor) {
        this.mInsetForeground = new ColorDrawable(mInsetForegroundColor);
    }

    public boolean isTintStatusBar() {
        return this.mTintStatusBar;
    }

    public void setTintStatusBar(boolean mTintStatusBar) {
        this.mTintStatusBar = mTintStatusBar;
    }

    public boolean isTintNavigationBar() {
        return this.mTintNavigationBar;
    }

    public void setTintNavigationBar(boolean mTintNavigationBar) {
        this.mTintNavigationBar = mTintNavigationBar;
    }

    public boolean isSystemUIVisible() {
        return this.mSystemUIVisible;
    }

    public void setSystemUIVisible(boolean systemUIVisible) {
        this.mSystemUIVisible = systemUIVisible;
    }

    public void setOnInsetsCallback(OnInsetsCallback onInsetsCallback) {
        this.mOnInsetsCallback = onInsetsCallback;
    }

    public OnInsetsCallback getOnInsetsCallback() {
        return this.mOnInsetsCallback;
    }
}
