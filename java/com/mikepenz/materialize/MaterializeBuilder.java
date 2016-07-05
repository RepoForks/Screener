package com.mikepenz.materialize;

import android.app.Activity;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.mikepenz.materialize.util.UIUtils;
import com.mikepenz.materialize.view.IScrimInsetsLayout;
import com.mikepenz.materialize.view.ScrimInsetsFrameLayout;

public class MaterializeBuilder {
    protected Activity mActivity;
    protected ViewGroup mContainer = null;
    protected LayoutParams mContainerLayoutParams = null;
    protected ViewGroup mContentRoot;
    protected boolean mFullscreen = false;
    protected boolean mNavigationBarPadding = false;
    protected ViewGroup mRootView;
    protected IScrimInsetsLayout mScrimInsetsLayout;
    protected int mStatusBarColor = 0;
    protected int mStatusBarColorRes = -1;
    protected boolean mStatusBarPadding = false;
    protected boolean mSystemUIHidden = false;
    protected boolean mTintNavigationBar = false;
    protected boolean mTintStatusBar = true;
    protected boolean mTranslucentNavigationBarProgrammatically = false;
    protected boolean mTranslucentStatusBarProgrammatically = false;
    protected boolean mTransparentNavigationBar = false;
    protected boolean mTransparentStatusBar = false;
    protected boolean mUseScrimInsetsLayout = true;

    public MaterializeBuilder(Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(16908290);
        this.mActivity = activity;
    }

    public MaterializeBuilder withActivity(Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(16908290);
        this.mActivity = activity;
        return this;
    }

    public MaterializeBuilder withRootView(ViewGroup rootView) {
        this.mRootView = rootView;
        return this;
    }

    public MaterializeBuilder withRootView(@IdRes int rootViewRes) {
        if (this.mActivity != null) {
            return withRootView((ViewGroup) this.mActivity.findViewById(rootViewRes));
        }
        throw new RuntimeException("please pass an activity first to use this call");
    }

    public MaterializeBuilder withUseScrimInsetsLayout(boolean useScrimInsetsLayout) {
        this.mUseScrimInsetsLayout = useScrimInsetsLayout;
        return this;
    }

    public MaterializeBuilder withStatusBarColor(@ColorInt int statusBarColor) {
        this.mStatusBarColor = statusBarColor;
        return this;
    }

    public MaterializeBuilder withStatusBarColorRes(@ColorRes int statusBarColorRes) {
        this.mStatusBarColorRes = statusBarColorRes;
        return this;
    }

    public MaterializeBuilder withTransparentStatusBar(boolean transparentStatusBar) {
        this.mTransparentStatusBar = transparentStatusBar;
        return this;
    }

    public MaterializeBuilder withTranslucentStatusBarProgrammatically(boolean translucentStatusBarProgrammatically) {
        this.mTranslucentStatusBarProgrammatically = translucentStatusBarProgrammatically;
        return this;
    }

    public MaterializeBuilder withStatusBarPadding(boolean statusBarPadding) {
        this.mStatusBarPadding = statusBarPadding;
        return this;
    }

    public MaterializeBuilder withTintedStatusBar(boolean tintedStatusBar) {
        this.mTintStatusBar = tintedStatusBar;
        return this;
    }

    public MaterializeBuilder withTranslucentNavigationBarProgrammatically(boolean translucentNavigationBarProgrammatically) {
        this.mTranslucentNavigationBarProgrammatically = translucentNavigationBarProgrammatically;
        return this;
    }

    public MaterializeBuilder withTransparentNavigationBar(boolean navigationBar) {
        this.mTransparentNavigationBar = navigationBar;
        return this;
    }

    public MaterializeBuilder withNavigationBarPadding(boolean navigationBarPadding) {
        this.mNavigationBarPadding = navigationBarPadding;
        return this;
    }

    public MaterializeBuilder withTintedNavigationBar(boolean tintedNavigationBar) {
        this.mTintNavigationBar = tintedNavigationBar;
        if (tintedNavigationBar) {
            withTranslucentNavigationBarProgrammatically(true);
        }
        return this;
    }

    public MaterializeBuilder withFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;
        if (fullscreen) {
            withTranslucentNavigationBarProgrammatically(true);
            withTintedStatusBar(false);
            withTintedNavigationBar(false);
        }
        return this;
    }

    public MaterializeBuilder withSystemUIHidden(boolean systemUIHidden) {
        this.mSystemUIHidden = systemUIHidden;
        if (systemUIHidden) {
            withFullscreen(systemUIHidden);
        }
        return this;
    }

    public MaterializeBuilder withContainer(ViewGroup container) {
        this.mContainer = container;
        return this;
    }

    public MaterializeBuilder withContainerLayoutParams(LayoutParams layoutParams) {
        this.mContainerLayoutParams = layoutParams;
        return this;
    }

    public MaterializeBuilder withContainer(ViewGroup container, LayoutParams layoutParams) {
        this.mContainer = container;
        this.mContainerLayoutParams = layoutParams;
        return this;
    }

    public Materialize build() {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity");
        }
        View originalContentView;
        if (this.mUseScrimInsetsLayout) {
            this.mScrimInsetsLayout = (ScrimInsetsFrameLayout) this.mActivity.getLayoutInflater().inflate(R.layout.materialize, this.mRootView, false);
            if (this.mRootView == null || this.mRootView.getChildCount() == 0) {
                throw new RuntimeException("You have to set your layout for this activity with setContentView() first. Or you build the drawer on your own with .buildView()");
            }
            boolean alreadyInflated;
            boolean z;
            originalContentView = this.mRootView.getChildAt(0);
            if (originalContentView.getId() == R.id.materialize_root) {
                alreadyInflated = true;
            } else {
                alreadyInflated = false;
            }
            if (this.mStatusBarColor == 0 && this.mStatusBarColorRes != -1) {
                this.mStatusBarColor = ContextCompat.getColor(this.mActivity, this.mStatusBarColorRes);
            } else if (this.mStatusBarColor == 0) {
                this.mStatusBarColor = UIUtils.getThemeColorFromAttrOrRes(this.mActivity, R.attr.colorPrimaryDark, R.color.materialize_primary_dark);
            }
            this.mScrimInsetsLayout.setInsetForeground(this.mStatusBarColor);
            this.mScrimInsetsLayout.setTintStatusBar(this.mTintStatusBar);
            this.mScrimInsetsLayout.setTintNavigationBar(this.mTintNavigationBar);
            IScrimInsetsLayout iScrimInsetsLayout = this.mScrimInsetsLayout;
            if (this.mFullscreen || this.mSystemUIHidden) {
                z = false;
            } else {
                z = true;
            }
            iScrimInsetsLayout.setSystemUIVisible(z);
            if (alreadyInflated) {
                this.mRootView.removeAllViews();
            } else {
                this.mRootView.removeView(originalContentView);
            }
            this.mScrimInsetsLayout.getView().addView(originalContentView, new FrameLayout.LayoutParams(-1, -1));
            this.mContentRoot = this.mScrimInsetsLayout.getView();
            if (this.mContainer != null) {
                this.mContentRoot = this.mContainer;
                this.mContentRoot.addView(this.mScrimInsetsLayout.getView(), new LayoutParams(-1, -1));
            }
            this.mContentRoot.setId(R.id.materialize_root);
            if (this.mContainerLayoutParams == null) {
                this.mContainerLayoutParams = new LayoutParams(-1, -1);
            }
            this.mRootView.addView(this.mContentRoot, this.mContainerLayoutParams);
        } else if (this.mContainer == null) {
            throw new RuntimeException("please pass a container");
        } else {
            originalContentView = this.mRootView.getChildAt(0);
            this.mRootView.removeView(originalContentView);
            this.mContainer.addView(originalContentView, new FrameLayout.LayoutParams(-1, -1));
            if (this.mContainerLayoutParams == null) {
                this.mContainerLayoutParams = new LayoutParams(-1, -1);
            }
            this.mRootView.addView(this.mContainer, this.mContainerLayoutParams);
        }
        if (this.mSystemUIHidden && VERSION.SDK_INT >= 16) {
            this.mActivity.getWindow().getDecorView().setSystemUiVisibility(5894);
        }
        if (this.mTranslucentStatusBarProgrammatically && VERSION.SDK_INT >= 21) {
            UIUtils.setTranslucentStatusFlag(this.mActivity, false);
        }
        if (this.mTranslucentNavigationBarProgrammatically && VERSION.SDK_INT >= 21) {
            UIUtils.setTranslucentNavigationFlag(this.mActivity, true);
        }
        if ((this.mTransparentStatusBar || this.mTransparentNavigationBar) && VERSION.SDK_INT >= 21) {
            this.mActivity.getWindow().addFlags(Integer.MIN_VALUE);
        }
        if (this.mTransparentStatusBar && VERSION.SDK_INT >= 21) {
            UIUtils.setTranslucentStatusFlag(this.mActivity, false);
            this.mActivity.getWindow().setStatusBarColor(0);
        }
        if (this.mTransparentNavigationBar && VERSION.SDK_INT >= 21) {
            UIUtils.setTranslucentNavigationFlag(this.mActivity, true);
            this.mActivity.getWindow().setNavigationBarColor(0);
        }
        int paddingTop = 0;
        if (this.mStatusBarPadding && VERSION.SDK_INT >= 21) {
            paddingTop = UIUtils.getStatusBarHeight(this.mActivity);
        }
        int paddingBottom = 0;
        if (this.mNavigationBarPadding && VERSION.SDK_INT >= 21) {
            paddingBottom = UIUtils.getNavigationBarHeight(this.mActivity);
        }
        if (this.mStatusBarPadding || (this.mNavigationBarPadding && VERSION.SDK_INT >= 21)) {
            this.mScrimInsetsLayout.getView().setPadding(0, paddingTop, 0, paddingBottom);
        }
        this.mActivity = null;
        return new Materialize(this);
    }
}
