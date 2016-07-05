package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.FastAdapter.OnLongClickListener;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemLongClickListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerNavigationListener;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialize.Materialize;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;
import com.mikepenz.materialize.view.ScrimInsetsRelativeLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawerBuilder {
    protected AccountHeader mAccountHeader;
    protected boolean mAccountHeaderSticky = false;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    protected boolean mActionBarDrawerToggleEnabled = true;
    protected Activity mActivity;
    protected FastAdapter<IDrawerItem> mAdapter;
    protected Adapter mAdapterWrapper;
    protected boolean mAnimateActionBarDrawerToggle = false;
    protected boolean mAppended = false;
    protected boolean mCloseOnClick = true;
    protected int mCurrentStickyFooterSelection = -1;
    protected View mCustomView;
    protected int mDelayDrawerClickEvent = 0;
    protected int mDelayOnDrawerClose = 50;
    protected Boolean mDisplayBelowStatusBar;
    protected Integer mDrawerGravity = Integer.valueOf(8388611);
    protected DrawerLayout mDrawerLayout;
    protected int mDrawerWidth = -1;
    protected boolean mFireInitialOnClick = false;
    protected FooterAdapter<IDrawerItem> mFooterAdapter = new FooterAdapter();
    protected boolean mFooterClickable = false;
    protected boolean mFooterDivider = true;
    protected View mFooterView;
    protected boolean mFullscreen = false;
    protected boolean mGenerateMiniDrawer = false;
    protected boolean mHasStableIds = false;
    protected HeaderAdapter<IDrawerItem> mHeaderAdapter = new HeaderAdapter();
    protected boolean mHeaderDivider = true;
    protected boolean mHeaderPadding = true;
    protected View mHeaderView;
    protected DimenHolder mHeiderHeight = null;
    private boolean mInnerShadow = false;
    protected ItemAdapter<IDrawerItem> mItemAdapter = new ItemAdapter();
    protected ItemAnimator mItemAnimator = new DefaultItemAnimator();
    protected LayoutManager mLayoutManager;
    protected Materialize mMaterialize;
    protected MiniDrawer mMiniDrawer = null;
    protected boolean mMultiSelect = false;
    protected OnDrawerItemClickListener mOnDrawerItemClickListener;
    protected OnDrawerItemLongClickListener mOnDrawerItemLongClickListener;
    protected OnDrawerListener mOnDrawerListener;
    protected OnDrawerNavigationListener mOnDrawerNavigationListener;
    protected boolean mPositionBasedStateManagement = true;
    protected RecyclerView mRecyclerView;
    protected ViewGroup mRootView;
    protected Bundle mSavedInstance;
    protected boolean mScrollToTopAfterClick = false;
    protected long mSelectedItemIdentifier = 0;
    protected int mSelectedItemPosition = 0;
    protected boolean mShowDrawerOnFirstLaunch = false;
    protected int mSliderBackgroundColor = 0;
    protected int mSliderBackgroundColorRes = -1;
    protected Drawable mSliderBackgroundDrawable = null;
    protected int mSliderBackgroundDrawableRes = -1;
    protected ScrimInsetsRelativeLayout mSliderLayout;
    protected List<IDrawerItem> mStickyDrawerItems = new ArrayList();
    protected boolean mStickyFooterDivider = false;
    protected boolean mStickyFooterShadow = true;
    protected View mStickyFooterShadowView;
    protected ViewGroup mStickyFooterView;
    protected boolean mStickyHeaderShadow = true;
    protected View mStickyHeaderView;
    protected boolean mSystemUIHidden = false;
    protected Toolbar mToolbar;
    protected boolean mTranslucentNavigationBar = false;
    protected boolean mTranslucentNavigationBarProgrammatically = false;
    protected boolean mTranslucentStatusBar = true;
    protected boolean mUsed = false;

    public DrawerBuilder() {
        getAdapter();
    }

    public DrawerBuilder(@NonNull Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(16908290);
        this.mActivity = activity;
        this.mLayoutManager = new LinearLayoutManager(this.mActivity);
        getAdapter();
    }

    public DrawerBuilder withActivity(@NonNull Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(16908290);
        this.mActivity = activity;
        this.mLayoutManager = new LinearLayoutManager(this.mActivity);
        return this;
    }

    public DrawerBuilder withRootView(@NonNull ViewGroup rootView) {
        this.mRootView = rootView;
        withTranslucentStatusBar(false);
        return this;
    }

    public DrawerBuilder withRootView(@IdRes int rootViewRes) {
        if (this.mActivity != null) {
            return withRootView((ViewGroup) this.mActivity.findViewById(rootViewRes));
        }
        throw new RuntimeException("please pass an activity first to use this call");
    }

    public DrawerBuilder withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
        return this;
    }

    public DrawerBuilder withDisplayBelowStatusBar(boolean displayBelowStatusBar) {
        this.mDisplayBelowStatusBar = Boolean.valueOf(displayBelowStatusBar);
        return this;
    }

    public DrawerBuilder withInnerShadow(boolean innerShadow) {
        this.mInnerShadow = innerShadow;
        return this;
    }

    public DrawerBuilder withToolbar(@NonNull Toolbar toolbar) {
        this.mToolbar = toolbar;
        return this;
    }

    public DrawerBuilder withTranslucentNavigationBar(boolean translucentNavigationBar) {
        this.mTranslucentNavigationBar = translucentNavigationBar;
        if (!translucentNavigationBar) {
            this.mTranslucentNavigationBarProgrammatically = false;
        }
        return this;
    }

    public DrawerBuilder withTranslucentNavigationBarProgrammatically(boolean translucentNavigationBarProgrammatically) {
        this.mTranslucentNavigationBarProgrammatically = translucentNavigationBarProgrammatically;
        if (translucentNavigationBarProgrammatically) {
            this.mTranslucentNavigationBar = true;
        }
        return this;
    }

    public DrawerBuilder withFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;
        if (fullscreen) {
            withTranslucentStatusBar(true);
            withTranslucentNavigationBar(false);
        }
        return this;
    }

    public DrawerBuilder withSystemUIHidden(boolean systemUIHidden) {
        this.mSystemUIHidden = systemUIHidden;
        if (systemUIHidden) {
            withFullscreen(systemUIHidden);
        }
        return this;
    }

    public DrawerBuilder withCustomView(@NonNull View customView) {
        this.mCustomView = customView;
        return this;
    }

    public DrawerBuilder withDrawerLayout(@NonNull DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        return this;
    }

    public DrawerBuilder withDrawerLayout(@LayoutRes int resLayout) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        if (resLayout != -1) {
            this.mDrawerLayout = (DrawerLayout) this.mActivity.getLayoutInflater().inflate(resLayout, this.mRootView, false);
        } else if (VERSION.SDK_INT < 21) {
            this.mDrawerLayout = (DrawerLayout) this.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_fits_not, this.mRootView, false);
        } else {
            this.mDrawerLayout = (DrawerLayout) this.mActivity.getLayoutInflater().inflate(R.layout.material_drawer, this.mRootView, false);
        }
        return this;
    }

    public DrawerBuilder withSliderBackgroundColor(@ColorInt int sliderBackgroundColor) {
        this.mSliderBackgroundColor = sliderBackgroundColor;
        return this;
    }

    public DrawerBuilder withSliderBackgroundColorRes(@ColorRes int sliderBackgroundColorRes) {
        this.mSliderBackgroundColorRes = sliderBackgroundColorRes;
        return this;
    }

    public DrawerBuilder withSliderBackgroundDrawable(@NonNull Drawable sliderBackgroundDrawable) {
        this.mSliderBackgroundDrawable = sliderBackgroundDrawable;
        return this;
    }

    public DrawerBuilder withSliderBackgroundDrawableRes(@DrawableRes int sliderBackgroundDrawableRes) {
        this.mSliderBackgroundDrawableRes = sliderBackgroundDrawableRes;
        return this;
    }

    public DrawerBuilder withDrawerWidthPx(int drawerWidthPx) {
        this.mDrawerWidth = drawerWidthPx;
        return this;
    }

    public DrawerBuilder withDrawerWidthDp(int drawerWidthDp) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        this.mDrawerWidth = Utils.convertDpToPx(this.mActivity, (float) drawerWidthDp);
        return this;
    }

    public DrawerBuilder withDrawerWidthRes(@DimenRes int drawerWidthRes) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        this.mDrawerWidth = this.mActivity.getResources().getDimensionPixelSize(drawerWidthRes);
        return this;
    }

    public DrawerBuilder withDrawerGravity(int gravity) {
        this.mDrawerGravity = Integer.valueOf(gravity);
        return this;
    }

    public DrawerBuilder withAccountHeader(@NonNull AccountHeader accountHeader) {
        return withAccountHeader(accountHeader, false);
    }

    public DrawerBuilder withAccountHeader(@NonNull AccountHeader accountHeader, boolean accountHeaderSticky) {
        this.mAccountHeader = accountHeader;
        this.mAccountHeaderSticky = accountHeaderSticky;
        return this;
    }

    public DrawerBuilder withActionBarDrawerToggleAnimated(boolean actionBarDrawerToggleAnimated) {
        this.mAnimateActionBarDrawerToggle = actionBarDrawerToggleAnimated;
        return this;
    }

    public DrawerBuilder withActionBarDrawerToggle(boolean actionBarDrawerToggleEnabled) {
        this.mActionBarDrawerToggleEnabled = actionBarDrawerToggleEnabled;
        return this;
    }

    public DrawerBuilder withActionBarDrawerToggle(@NonNull ActionBarDrawerToggle actionBarDrawerToggle) {
        this.mActionBarDrawerToggleEnabled = true;
        this.mActionBarDrawerToggle = actionBarDrawerToggle;
        return this;
    }

    public DrawerBuilder withScrollToTopAfterClick(boolean scrollToTopAfterClick) {
        this.mScrollToTopAfterClick = scrollToTopAfterClick;
        return this;
    }

    public DrawerBuilder withHeader(@NonNull View headerView) {
        this.mHeaderView = headerView;
        return this;
    }

    public DrawerBuilder withHeader(@LayoutRes int headerViewRes) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        if (headerViewRes != -1) {
            this.mHeaderView = this.mActivity.getLayoutInflater().inflate(headerViewRes, null, false);
        }
        return this;
    }

    public DrawerBuilder withHeaderDivider(boolean headerDivider) {
        this.mHeaderDivider = headerDivider;
        return this;
    }

    public DrawerBuilder withHeaderPadding(boolean headerPadding) {
        this.mHeaderPadding = headerPadding;
        return this;
    }

    public DrawerBuilder withHeaderHeight(DimenHolder headerHeight) {
        this.mHeiderHeight = headerHeight;
        return this;
    }

    public DrawerBuilder withStickyHeader(@NonNull View stickyHeader) {
        this.mStickyHeaderView = stickyHeader;
        return this;
    }

    public DrawerBuilder withStickyHeader(@LayoutRes int stickyHeaderRes) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        if (stickyHeaderRes != -1) {
            this.mStickyHeaderView = this.mActivity.getLayoutInflater().inflate(stickyHeaderRes, null, false);
        }
        return this;
    }

    public DrawerBuilder withStickyHeaderShadow(boolean stickyHeaderShadow) {
        this.mStickyHeaderShadow = stickyHeaderShadow;
        return this;
    }

    public DrawerBuilder withFooter(@NonNull View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    public DrawerBuilder withFooter(@LayoutRes int footerViewRes) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        if (footerViewRes != -1) {
            this.mFooterView = this.mActivity.getLayoutInflater().inflate(footerViewRes, null, false);
        }
        return this;
    }

    public DrawerBuilder withFooterClickable(boolean footerClickable) {
        this.mFooterClickable = footerClickable;
        return this;
    }

    public DrawerBuilder withFooterDivider(boolean footerDivider) {
        this.mFooterDivider = footerDivider;
        return this;
    }

    public DrawerBuilder withStickyFooter(@NonNull ViewGroup stickyFooter) {
        this.mStickyFooterView = stickyFooter;
        return this;
    }

    public DrawerBuilder withStickyFooter(@LayoutRes int stickyFooterRes) {
        if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }
        if (stickyFooterRes != -1) {
            this.mStickyFooterView = (ViewGroup) this.mActivity.getLayoutInflater().inflate(stickyFooterRes, null, false);
        }
        return this;
    }

    public DrawerBuilder withStickyFooterDivider(boolean stickyFooterDivider) {
        this.mStickyFooterDivider = stickyFooterDivider;
        return this;
    }

    public DrawerBuilder withStickyFooterShadow(boolean stickyFooterShadow) {
        this.mStickyFooterShadow = stickyFooterShadow;
        return this;
    }

    public DrawerBuilder withFireOnInitialOnClick(boolean fireOnInitialOnClick) {
        this.mFireInitialOnClick = fireOnInitialOnClick;
        return this;
    }

    public DrawerBuilder withMultiSelect(boolean multiSelect) {
        this.mMultiSelect = multiSelect;
        return this;
    }

    public DrawerBuilder withSelectedItemByPosition(int selectedItemPosition) {
        this.mSelectedItemPosition = selectedItemPosition;
        return this;
    }

    public DrawerBuilder withSelectedItem(long selectedItemIdentifier) {
        this.mSelectedItemIdentifier = selectedItemIdentifier;
        return this;
    }

    public DrawerBuilder withRecyclerView(@NonNull RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        return this;
    }

    public DrawerBuilder withHasStableIds(boolean hasStableIds) {
        this.mHasStableIds = hasStableIds;
        if (this.mAdapter != null) {
            this.mAdapter.setHasStableIds(hasStableIds);
        }
        return this;
    }

    public DrawerBuilder withPositionBasedStateManagement(boolean positionBasedStateManagement) {
        this.mPositionBasedStateManagement = positionBasedStateManagement;
        return this;
    }

    public DrawerBuilder withAdapter(@NonNull FastAdapter<IDrawerItem> adapter) {
        this.mAdapter = adapter;
        this.mHeaderAdapter.wrap(this.mItemAdapter.wrap(this.mFooterAdapter.wrap(this.mAdapter)));
        return this;
    }

    protected FastAdapter<IDrawerItem> getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new FastAdapter();
            this.mAdapter.withSelectable(true);
            this.mAdapter.withAllowDeselection(false);
            this.mAdapter.setHasStableIds(this.mHasStableIds);
            this.mAdapter.withPositionBasedStateManagement(this.mPositionBasedStateManagement);
            this.mHeaderAdapter.wrap(this.mItemAdapter.wrap(this.mFooterAdapter.wrap(this.mAdapter)));
        }
        return this.mAdapter;
    }

    protected IItemAdapter<IDrawerItem> getItemAdapter() {
        return this.mItemAdapter;
    }

    protected IItemAdapter<IDrawerItem> getHeaderAdapter() {
        return this.mHeaderAdapter;
    }

    protected IItemAdapter<IDrawerItem> getFooterAdapter() {
        return this.mFooterAdapter;
    }

    public DrawerBuilder withAdapterWrapper(@NonNull Adapter adapterWrapper) {
        if (this.mAdapter == null) {
            throw new RuntimeException("this adapter has to be set in conjunction to a normal adapter which is used inside this wrapper adapter");
        }
        this.mAdapterWrapper = adapterWrapper;
        return this;
    }

    public DrawerBuilder withItemAnimator(ItemAnimator itemAnimator) {
        this.mItemAnimator = itemAnimator;
        return this;
    }

    public DrawerBuilder withDrawerItems(@NonNull List<IDrawerItem> drawerItems) {
        getItemAdapter().set(drawerItems);
        return this;
    }

    public DrawerBuilder addDrawerItems(@NonNull IDrawerItem... drawerItems) {
        getItemAdapter().add((IItem[]) drawerItems);
        return this;
    }

    public DrawerBuilder withStickyDrawerItems(@NonNull List<IDrawerItem> stickyDrawerItems) {
        this.mStickyDrawerItems = stickyDrawerItems;
        return this;
    }

    public DrawerBuilder addStickyDrawerItems(@NonNull IDrawerItem... stickyDrawerItems) {
        if (this.mStickyDrawerItems == null) {
            this.mStickyDrawerItems = new ArrayList();
        }
        Collections.addAll(this.mStickyDrawerItems, stickyDrawerItems);
        return this;
    }

    public DrawerBuilder inflateMenu(@MenuRes int menuRes) {
        MenuInflater menuInflater = new SupportMenuInflater(this.mActivity);
        MenuBuilder mMenu = new MenuBuilder(this.mActivity);
        menuInflater.inflate(menuRes, mMenu);
        addMenuItems(mMenu, false);
        return this;
    }

    private void addMenuItems(Menu mMenu, boolean subMenu) {
        int groupId = R.id.material_drawer_menu_default_group;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem mMenuItem = mMenu.getItem(i);
            if (!(subMenu || mMenuItem.getGroupId() == groupId || mMenuItem.getGroupId() == 0)) {
                groupId = mMenuItem.getGroupId();
                IDrawerItem iDrawerItem = new DividerDrawerItem();
                getItemAdapter().add(iDrawerItem);
            }
            if (mMenuItem.hasSubMenu()) {
                iDrawerItem = (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withName(mMenuItem.getTitle().toString())).withIcon(mMenuItem.getIcon())).withIdentifier((long) mMenuItem.getItemId())).withEnabled(mMenuItem.isEnabled())).withSelectable(false);
                getItemAdapter().add(iDrawerItem);
                addMenuItems(mMenuItem.getSubMenu(), true);
            } else if (mMenuItem.getGroupId() != 0 || subMenu) {
                iDrawerItem = (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new SecondaryDrawerItem().withName(mMenuItem.getTitle().toString())).withIcon(mMenuItem.getIcon())).withIdentifier((long) mMenuItem.getItemId())).withEnabled(mMenuItem.isEnabled());
                getItemAdapter().add(iDrawerItem);
            } else {
                iDrawerItem = (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withName(mMenuItem.getTitle().toString())).withIcon(mMenuItem.getIcon())).withIdentifier((long) mMenuItem.getItemId())).withEnabled(mMenuItem.isEnabled());
                getItemAdapter().add(iDrawerItem);
            }
        }
    }

    public DrawerBuilder withCloseOnClick(boolean closeOnClick) {
        this.mCloseOnClick = closeOnClick;
        return this;
    }

    public DrawerBuilder withDelayOnDrawerClose(int delayOnDrawerClose) {
        this.mDelayOnDrawerClose = delayOnDrawerClose;
        return this;
    }

    public DrawerBuilder withDelayDrawerClickEvent(int delayDrawerClickEvent) {
        this.mDelayDrawerClickEvent = delayDrawerClickEvent;
        return this;
    }

    public DrawerBuilder withOnDrawerListener(@NonNull OnDrawerListener onDrawerListener) {
        this.mOnDrawerListener = onDrawerListener;
        return this;
    }

    public DrawerBuilder withOnDrawerItemClickListener(@NonNull OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    public DrawerBuilder withOnDrawerItemLongClickListener(@NonNull OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
        this.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
        return this;
    }

    public DrawerBuilder withOnDrawerNavigationListener(@NonNull OnDrawerNavigationListener onDrawerNavigationListener) {
        this.mOnDrawerNavigationListener = onDrawerNavigationListener;
        return this;
    }

    public DrawerBuilder withShowDrawerOnFirstLaunch(boolean showDrawerOnFirstLaunch) {
        this.mShowDrawerOnFirstLaunch = showDrawerOnFirstLaunch;
        return this;
    }

    public DrawerBuilder withGenerateMiniDrawer(boolean generateMiniDrawer) {
        this.mGenerateMiniDrawer = generateMiniDrawer;
        return this;
    }

    public DrawerBuilder withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
        return this;
    }

    private void handleShowOnFirstLaunch() {
        if (this.mActivity != null && this.mDrawerLayout != null && this.mShowDrawerOnFirstLaunch) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mActivity);
            if (!preferences.getBoolean("navigation_drawer_learned", false)) {
                this.mDrawerLayout.openDrawer(this.mSliderLayout);
                Editor editor = preferences.edit();
                editor.putBoolean("navigation_drawer_learned", true);
                editor.apply();
            }
        }
    }

    public Drawer build() {
        if (this.mUsed) {
            throw new RuntimeException("you must not reuse a DrawerBuilder builder");
        } else if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity");
        } else {
            this.mUsed = true;
            if (this.mDrawerLayout == null) {
                withDrawerLayout(-1);
            }
            this.mMaterialize = new MaterializeBuilder().withActivity(this.mActivity).withRootView(this.mRootView).withFullscreen(this.mFullscreen).withSystemUIHidden(this.mSystemUIHidden).withUseScrimInsetsLayout(false).withTransparentStatusBar(this.mTranslucentStatusBar).withTranslucentNavigationBarProgrammatically(this.mTranslucentNavigationBarProgrammatically).withContainer(this.mDrawerLayout).build();
            handleDrawerNavigation(this.mActivity, false);
            Drawer result = buildView();
            this.mSliderLayout.setId(R.id.material_drawer_slider_layout);
            this.mDrawerLayout.addView(this.mSliderLayout, 1);
            return result;
        }
    }

    public Drawer buildForFragment() {
        if (this.mUsed) {
            throw new RuntimeException("you must not reuse a DrawerBuilder builder");
        } else if (this.mActivity == null) {
            throw new RuntimeException("please pass an activity");
        } else if (this.mRootView == null) {
            throw new RuntimeException("please pass the view which should host the DrawerLayout");
        } else {
            boolean alreadyInflated;
            this.mUsed = true;
            if (this.mDrawerLayout == null) {
                withDrawerLayout(-1);
            }
            View originalContentView = this.mRootView.getChildAt(0);
            if (originalContentView.getId() == R.id.materialize_root) {
                alreadyInflated = true;
            } else {
                alreadyInflated = false;
            }
            if (alreadyInflated) {
                this.mRootView.removeAllViews();
            } else {
                this.mRootView.removeView(originalContentView);
            }
            this.mRootView.addView(this.mDrawerLayout, new LayoutParams(-1, -1));
            this.mDrawerLayout.setId(R.id.materialize_root);
            handleDrawerNavigation(this.mActivity, false);
            Drawer result = buildView();
            this.mDrawerLayout.addView(originalContentView, 0);
            this.mSliderLayout.setId(R.id.material_drawer_slider_layout);
            this.mDrawerLayout.addView(this.mSliderLayout, 1);
            return result;
        }
    }

    protected void handleDrawerNavigation(Activity activity, boolean recreateActionBarDrawerToggle) {
        OnClickListener toolbarNavigationListener = new OnClickListener() {
            public void onClick(View v) {
                boolean handled = false;
                if (!(DrawerBuilder.this.mOnDrawerNavigationListener == null || DrawerBuilder.this.mActionBarDrawerToggle == null || DrawerBuilder.this.mActionBarDrawerToggle.isDrawerIndicatorEnabled())) {
                    handled = DrawerBuilder.this.mOnDrawerNavigationListener.onNavigationClickListener(v);
                }
                if (!handled) {
                    if (DrawerBuilder.this.mDrawerLayout.isDrawerOpen(DrawerBuilder.this.mDrawerGravity.intValue())) {
                        DrawerBuilder.this.mDrawerLayout.closeDrawer(DrawerBuilder.this.mDrawerGravity.intValue());
                    } else {
                        DrawerBuilder.this.mDrawerLayout.openDrawer(DrawerBuilder.this.mDrawerGravity.intValue());
                    }
                }
            }
        };
        if (recreateActionBarDrawerToggle) {
            this.mActionBarDrawerToggle = null;
        }
        if (this.mActionBarDrawerToggleEnabled && this.mActionBarDrawerToggle == null && this.mToolbar != null) {
            this.mActionBarDrawerToggle = new ActionBarDrawerToggle(activity, this.mDrawerLayout, this.mToolbar, R.string.material_drawer_open, R.string.material_drawer_close) {
                public void onDrawerOpened(View drawerView) {
                    if (DrawerBuilder.this.mOnDrawerListener != null) {
                        DrawerBuilder.this.mOnDrawerListener.onDrawerOpened(drawerView);
                    }
                    super.onDrawerOpened(drawerView);
                }

                public void onDrawerClosed(View drawerView) {
                    if (DrawerBuilder.this.mOnDrawerListener != null) {
                        DrawerBuilder.this.mOnDrawerListener.onDrawerClosed(drawerView);
                    }
                    super.onDrawerClosed(drawerView);
                }

                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (DrawerBuilder.this.mOnDrawerListener != null) {
                        DrawerBuilder.this.mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }
                    if (DrawerBuilder.this.mAnimateActionBarDrawerToggle) {
                        super.onDrawerSlide(drawerView, slideOffset);
                    } else {
                        super.onDrawerSlide(drawerView, 0.0f);
                    }
                }
            };
            this.mActionBarDrawerToggle.syncState();
        }
        if (this.mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(toolbarNavigationListener);
        }
        if (this.mActionBarDrawerToggle != null) {
            this.mActionBarDrawerToggle.setToolbarNavigationClickListener(toolbarNavigationListener);
            this.mDrawerLayout.setDrawerListener(this.mActionBarDrawerToggle);
            return;
        }
        this.mDrawerLayout.setDrawerListener(new DrawerListener() {
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (DrawerBuilder.this.mOnDrawerListener != null) {
                    DrawerBuilder.this.mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                }
            }

            public void onDrawerOpened(View drawerView) {
                if (DrawerBuilder.this.mOnDrawerListener != null) {
                    DrawerBuilder.this.mOnDrawerListener.onDrawerOpened(drawerView);
                }
            }

            public void onDrawerClosed(View drawerView) {
                if (DrawerBuilder.this.mOnDrawerListener != null) {
                    DrawerBuilder.this.mOnDrawerListener.onDrawerClosed(drawerView);
                }
            }

            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    public Drawer buildView() {
        this.mSliderLayout = (ScrimInsetsRelativeLayout) this.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, this.mDrawerLayout, false);
        this.mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this.mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) this.mSliderLayout.getLayoutParams();
        if (params != null) {
            params.gravity = this.mDrawerGravity.intValue();
            this.mSliderLayout.setLayoutParams(DrawerUtils.processDrawerLayoutParams(this, params));
        }
        createContent();
        Drawer result = new Drawer(this);
        if (this.mAccountHeader != null) {
            this.mAccountHeader.setDrawer(result);
        }
        if (this.mSavedInstance != null && this.mSavedInstance.getBoolean("bundle_drawer_content_switched", false)) {
            this.mAccountHeader.toggleSelectionList(this.mActivity);
        }
        handleShowOnFirstLaunch();
        if (!this.mAppended && this.mGenerateMiniDrawer) {
            this.mMiniDrawer = new MiniDrawer().withDrawer(result).withAccountHeader(this.mAccountHeader).withPositionBasedStateManagement(this.mPositionBasedStateManagement);
        }
        this.mActivity = null;
        return result;
    }

    public Drawer append(@NonNull Drawer result) {
        if (this.mUsed) {
            throw new RuntimeException("you must not reuse a DrawerBuilder builder");
        } else if (this.mDrawerGravity == null) {
            throw new RuntimeException("please set the gravity for the drawer");
        } else {
            this.mUsed = true;
            this.mAppended = true;
            this.mDrawerLayout = result.getDrawerLayout();
            this.mSliderLayout = (ScrimInsetsRelativeLayout) this.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, this.mDrawerLayout, false);
            this.mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this.mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) this.mSliderLayout.getLayoutParams();
            params.gravity = this.mDrawerGravity.intValue();
            this.mSliderLayout.setLayoutParams(DrawerUtils.processDrawerLayoutParams(this, params));
            this.mSliderLayout.setId(R.id.material_drawer_slider_layout);
            this.mDrawerLayout.addView(this.mSliderLayout, 1);
            createContent();
            Drawer appendedResult = new Drawer(this);
            if (this.mSavedInstance != null && this.mSavedInstance.getBoolean("bundle_drawer_content_switched_appended", false)) {
                this.mAccountHeader.toggleSelectionList(this.mActivity);
            }
            this.mActivity = null;
            return appendedResult;
        }
    }

    private void createContent() {
        int selection = -1;
        if (this.mCustomView != null) {
            LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(-1, -1);
            contentParams.weight = 1.0f;
            this.mSliderLayout.addView(this.mCustomView, contentParams);
            return;
        }
        View contentView;
        if (VERSION.SDK_INT < 21 && this.mDrawerLayout != null) {
            if (ViewCompat.getLayoutDirection(this.mRootView) == 0) {
                this.mDrawerLayout.setDrawerShadow(this.mDrawerGravity.intValue() == 8388611 ? R.drawable.material_drawer_shadow_right : R.drawable.material_drawer_shadow_left, this.mDrawerGravity.intValue());
            } else {
                this.mDrawerLayout.setDrawerShadow(this.mDrawerGravity.intValue() == 8388611 ? R.drawable.material_drawer_shadow_left : R.drawable.material_drawer_shadow_right, this.mDrawerGravity.intValue());
            }
        }
        if (this.mRecyclerView == null) {
            contentView = LayoutInflater.from(this.mActivity).inflate(R.layout.material_drawer_recycler_view, this.mSliderLayout, false);
            this.mRecyclerView = (RecyclerView) contentView.findViewById(R.id.material_drawer_recycler_view);
            this.mRecyclerView.setItemAnimator(this.mItemAnimator);
            this.mRecyclerView.setFadingEdgeLength(0);
            this.mRecyclerView.setClipToPadding(false);
            this.mRecyclerView.setLayoutManager(this.mLayoutManager);
            int paddingTop = 0;
            if ((this.mDisplayBelowStatusBar == null || this.mDisplayBelowStatusBar.booleanValue()) && !this.mSystemUIHidden) {
                paddingTop = UIUtils.getStatusBarHeight(this.mActivity);
            }
            int paddingBottom = 0;
            if ((this.mTranslucentNavigationBar || this.mFullscreen) && VERSION.SDK_INT >= 21 && !this.mSystemUIHidden && this.mActivity.getResources().getConfiguration().orientation == 1) {
                paddingBottom = UIUtils.getNavigationBarHeight(this.mActivity);
            }
            this.mRecyclerView.setPadding(0, paddingTop, 0, paddingBottom);
        } else {
            contentView = this.mRecyclerView;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
        params.weight = 1.0f;
        this.mSliderLayout.addView(contentView, params);
        if (this.mInnerShadow) {
            View innerShadow = this.mSliderLayout.findViewById(R.id.material_drawer_inner_shadow);
            innerShadow.setVisibility(0);
            innerShadow.bringToFront();
            if (this.mDrawerGravity.intValue() == 8388611) {
                innerShadow.setBackgroundResource(R.drawable.material_drawer_shadow_left);
            } else {
                innerShadow.setBackgroundResource(R.drawable.material_drawer_shadow_right);
            }
        }
        if (this.mSliderBackgroundColor != 0) {
            this.mSliderLayout.setBackgroundColor(this.mSliderBackgroundColor);
        } else if (this.mSliderBackgroundColorRes != -1) {
            this.mSliderLayout.setBackgroundColor(ContextCompat.getColor(this.mActivity, this.mSliderBackgroundColorRes));
        } else if (this.mSliderBackgroundDrawable != null) {
            UIUtils.setBackground(this.mSliderLayout, this.mSliderBackgroundDrawable);
        } else if (this.mSliderBackgroundDrawableRes != -1) {
            UIUtils.setBackground(this.mSliderLayout, this.mSliderBackgroundDrawableRes);
        }
        DrawerUtils.handleHeaderView(this);
        DrawerUtils.handleFooterView(this, new OnClickListener() {
            public void onClick(View v) {
                DrawerUtils.onFooterDrawerItemClick(DrawerBuilder.this, (IDrawerItem) v.getTag(), v, Boolean.valueOf(true));
            }
        });
        this.mAdapter.withMultiSelect(this.mMultiSelect);
        if (this.mMultiSelect) {
            this.mAdapter.withSelectOnLongClick(false);
            this.mAdapter.withAllowDeselection(true);
        }
        if (this.mAdapterWrapper == null) {
            this.mRecyclerView.setAdapter(this.mAdapter);
        } else {
            this.mRecyclerView.setAdapter(this.mAdapterWrapper);
        }
        if (this.mSelectedItemPosition == 0 && this.mSelectedItemIdentifier != 0) {
            this.mSelectedItemPosition = DrawerUtils.getPositionByIdentifier(this, this.mSelectedItemIdentifier);
        }
        if (this.mHeaderView != null && this.mSelectedItemPosition == 0) {
            this.mSelectedItemPosition = 1;
        }
        this.mAdapter.deselect();
        this.mAdapter.select(this.mSelectedItemPosition);
        this.mAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDrawerItem>() {
            public boolean onClick(final View view, IAdapter<IDrawerItem> iAdapter, final IDrawerItem item, final int position) {
                if (item == null || !(item instanceof Selectable) || item.isSelectable()) {
                    DrawerBuilder.this.resetStickyFooterSelection();
                    DrawerBuilder.this.mCurrentStickyFooterSelection = -1;
                }
                boolean consumed = false;
                if ((item instanceof AbstractDrawerItem) && ((AbstractDrawerItem) item).getOnDrawerItemClickListener() != null) {
                    consumed = ((AbstractDrawerItem) item).getOnDrawerItemClickListener().onItemClick(view, position, item);
                }
                if (DrawerBuilder.this.mOnDrawerItemClickListener != null) {
                    if (DrawerBuilder.this.mDelayDrawerClickEvent > 0) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                DrawerBuilder.this.mOnDrawerItemClickListener.onItemClick(view, position, item);
                            }
                        }, (long) DrawerBuilder.this.mDelayDrawerClickEvent);
                    } else {
                        consumed = DrawerBuilder.this.mOnDrawerItemClickListener.onItemClick(view, position, item);
                    }
                }
                if (!(consumed || DrawerBuilder.this.mMiniDrawer == null)) {
                    consumed = DrawerBuilder.this.mMiniDrawer.onItemClick(item);
                }
                if ((item instanceof IExpandable) && ((IExpandable) item).getSubItems() != null) {
                    return true;
                }
                if (consumed) {
                    return consumed;
                }
                DrawerBuilder.this.closeDrawerDelayed();
                return consumed;
            }
        });
        this.mAdapter.withOnLongClickListener(new OnLongClickListener<IDrawerItem>() {
            public boolean onLongClick(View view, IAdapter<IDrawerItem> iAdapter, IDrawerItem item, int position) {
                if (DrawerBuilder.this.mOnDrawerItemLongClickListener != null) {
                    return DrawerBuilder.this.mOnDrawerItemLongClickListener.onItemLongClick(view, position, DrawerBuilder.this.getDrawerItem(position));
                }
                return false;
            }
        });
        if (this.mRecyclerView != null) {
            this.mRecyclerView.scrollToPosition(0);
        }
        if (this.mSavedInstance != null) {
            if (this.mAppended) {
                this.mAdapter.withSavedInstanceState(this.mSavedInstance, "_selection_appended");
                DrawerUtils.setStickyFooterSelection(this, this.mSavedInstance.getInt("bundle_sticky_footer_selection_appended", -1), null);
            } else {
                this.mAdapter.withSavedInstanceState(this.mSavedInstance, "_selection");
                DrawerUtils.setStickyFooterSelection(this, this.mSavedInstance.getInt("bundle_sticky_footer_selection", -1), null);
            }
        }
        if (this.mFireInitialOnClick && this.mOnDrawerItemClickListener != null) {
            if (this.mAdapter.getSelections().size() != 0) {
                selection = ((Integer) this.mAdapter.getSelections().iterator().next()).intValue();
            }
            this.mOnDrawerItemClickListener.onItemClick(null, selection, getDrawerItem(selection));
        }
    }

    protected void closeDrawerDelayed() {
        if (this.mCloseOnClick && this.mDrawerLayout != null) {
            if (this.mDelayOnDrawerClose > -1) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        DrawerBuilder.this.mDrawerLayout.closeDrawers();
                        if (DrawerBuilder.this.mScrollToTopAfterClick) {
                            DrawerBuilder.this.mRecyclerView.smoothScrollToPosition(0);
                        }
                    }
                }, (long) this.mDelayOnDrawerClose);
            } else {
                this.mDrawerLayout.closeDrawers();
            }
        }
    }

    protected IDrawerItem getDrawerItem(int position) {
        return (IDrawerItem) getAdapter().getItem(position);
    }

    protected boolean checkDrawerItem(int position, boolean includeOffset) {
        return getAdapter().getItem(position) != null;
    }

    protected void resetStickyFooterSelection() {
        if (this.mStickyFooterView instanceof LinearLayout) {
            for (int i = 0; i < this.mStickyFooterView.getChildCount(); i++) {
                if (VERSION.SDK_INT >= 11) {
                    this.mStickyFooterView.getChildAt(i).setActivated(false);
                }
                this.mStickyFooterView.getChildAt(i).setSelected(false);
            }
        }
    }
}
