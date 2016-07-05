package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem.Position;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialize.Materialize;
import com.mikepenz.materialize.view.ScrimInsetsRelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class Drawer {
    protected static final String BUNDLE_DRAWER_CONTENT_SWITCHED = "bundle_drawer_content_switched";
    protected static final String BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED = "bundle_drawer_content_switched_appended";
    protected static final String BUNDLE_SELECTION = "_selection";
    protected static final String BUNDLE_SELECTION_APPENDED = "_selection_appended";
    protected static final String BUNDLE_STICKY_FOOTER_SELECTION = "bundle_sticky_footer_selection";
    protected static final String BUNDLE_STICKY_FOOTER_SELECTION_APPENDED = "bundle_sticky_footer_selection_appended";
    protected static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private FrameLayout mContentView;
    protected final DrawerBuilder mDrawerBuilder;
    private List<IDrawerItem> originalDrawerItems;
    private Bundle originalDrawerState;
    private OnDrawerItemClickListener originalOnDrawerItemClickListener;
    private OnDrawerItemLongClickListener originalOnDrawerItemLongClickListener;

    public interface OnDrawerItemClickListener {
        boolean onItemClick(View view, int i, IDrawerItem iDrawerItem);
    }

    public interface OnDrawerItemLongClickListener {
        boolean onItemLongClick(View view, int i, IDrawerItem iDrawerItem);
    }

    public interface OnDrawerItemSelectedListener {
        void onItemSelected(AdapterView<?> adapterView, View view, int i, long j, IDrawerItem iDrawerItem);

        void onNothingSelected(AdapterView<?> adapterView);
    }

    public interface OnDrawerListener {
        void onDrawerClosed(View view);

        void onDrawerOpened(View view);

        void onDrawerSlide(View view, float f);
    }

    public interface OnDrawerNavigationListener {
        boolean onNavigationClickListener(View view);
    }

    protected Drawer(DrawerBuilder drawerBuilder) {
        this.mDrawerBuilder = drawerBuilder;
    }

    protected DrawerBuilder getDrawerBuilder() {
        return this.mDrawerBuilder;
    }

    public DrawerLayout getDrawerLayout() {
        return this.mDrawerBuilder.mDrawerLayout;
    }

    public void setToolbar(@NonNull Activity activity, @NonNull Toolbar toolbar) {
        setToolbar(activity, toolbar, false);
    }

    public void setToolbar(@NonNull Activity activity, @NonNull Toolbar toolbar, boolean recreateActionBarDrawerToggle) {
        this.mDrawerBuilder.mToolbar = toolbar;
        this.mDrawerBuilder.handleDrawerNavigation(activity, recreateActionBarDrawerToggle);
    }

    public void setActionBarDrawerToggle(@NonNull ActionBarDrawerToggle actionBarDrawerToggle) {
        this.mDrawerBuilder.mActionBarDrawerToggleEnabled = true;
        this.mDrawerBuilder.mActionBarDrawerToggle = actionBarDrawerToggle;
        this.mDrawerBuilder.handleDrawerNavigation(null, false);
    }

    public void openDrawer() {
        if (this.mDrawerBuilder.mDrawerLayout != null && this.mDrawerBuilder.mSliderLayout != null) {
            this.mDrawerBuilder.mDrawerLayout.openDrawer(this.mDrawerBuilder.mDrawerGravity.intValue());
        }
    }

    public void closeDrawer() {
        if (this.mDrawerBuilder.mDrawerLayout != null) {
            this.mDrawerBuilder.mDrawerLayout.closeDrawer(this.mDrawerBuilder.mDrawerGravity.intValue());
        }
    }

    public boolean isDrawerOpen() {
        if (this.mDrawerBuilder.mDrawerLayout == null || this.mDrawerBuilder.mSliderLayout == null) {
            return false;
        }
        return this.mDrawerBuilder.mDrawerLayout.isDrawerOpen(this.mDrawerBuilder.mDrawerGravity.intValue());
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.mDrawerBuilder.mMaterialize != null) {
            this.mDrawerBuilder.mMaterialize.setFullscreen(fullscreen);
        }
    }

    public Materialize getMaterialize() {
        return this.mDrawerBuilder.mMaterialize;
    }

    public MiniDrawer getMiniDrawer() {
        if (this.mDrawerBuilder.mMiniDrawer == null) {
            this.mDrawerBuilder.mMiniDrawer = new MiniDrawer().withDrawer(this).withAccountHeader(this.mDrawerBuilder.mAccountHeader).withPositionBasedStateManagement(this.mDrawerBuilder.mPositionBasedStateManagement);
        }
        return this.mDrawerBuilder.mMiniDrawer;
    }

    public ScrimInsetsRelativeLayout getSlider() {
        return this.mDrawerBuilder.mSliderLayout;
    }

    public FrameLayout getContent() {
        if (this.mContentView == null && this.mDrawerBuilder.mDrawerLayout != null) {
            this.mContentView = (FrameLayout) this.mDrawerBuilder.mDrawerLayout.findViewById(R.id.content_layout);
        }
        return this.mContentView;
    }

    public RecyclerView getRecyclerView() {
        return this.mDrawerBuilder.mRecyclerView;
    }

    public FastAdapter<IDrawerItem> getAdapter() {
        return this.mDrawerBuilder.mAdapter;
    }

    public HeaderAdapter<IDrawerItem> getHeaderAdapter() {
        return this.mDrawerBuilder.mHeaderAdapter;
    }

    public ItemAdapter<IDrawerItem> getItemAdapter() {
        return this.mDrawerBuilder.mItemAdapter;
    }

    public FooterAdapter<IDrawerItem> getFooterAdapter() {
        return this.mDrawerBuilder.mFooterAdapter;
    }

    public List<IDrawerItem> getDrawerItems() {
        return this.mDrawerBuilder.getItemAdapter().getAdapterItems();
    }

    public View getHeader() {
        return this.mDrawerBuilder.mHeaderView;
    }

    public View getStickyHeader() {
        return this.mDrawerBuilder.mStickyHeaderView;
    }

    public void setHeader(@NonNull View view) {
        setHeader(view, true, true);
    }

    public void setHeader(@NonNull View view, boolean divider) {
        setHeader(view, true, divider);
    }

    public void setHeader(@NonNull View view, boolean padding, boolean divider) {
        setHeader(view, padding, divider, null);
    }

    public void setHeader(@NonNull View view, boolean padding, boolean divider, DimenHolder height) {
        this.mDrawerBuilder.getHeaderAdapter().clear();
        if (padding) {
            this.mDrawerBuilder.getHeaderAdapter().add(new ContainerDrawerItem().withView(view).withDivider(divider).withHeight(height).withViewPosition(Position.TOP));
        } else {
            this.mDrawerBuilder.getHeaderAdapter().add(new ContainerDrawerItem().withView(view).withDivider(divider).withHeight(height).withViewPosition(Position.NONE));
        }
        this.mDrawerBuilder.mRecyclerView.setPadding(this.mDrawerBuilder.mRecyclerView.getPaddingLeft(), 0, this.mDrawerBuilder.mRecyclerView.getPaddingRight(), this.mDrawerBuilder.mRecyclerView.getPaddingBottom());
    }

    public void removeHeader() {
        this.mDrawerBuilder.getHeaderAdapter().clear();
    }

    public View getFooter() {
        return this.mDrawerBuilder.mFooterView;
    }

    public View getStickyFooter() {
        return this.mDrawerBuilder.mStickyFooterView;
    }

    private View getStickyFooterShadow() {
        return this.mDrawerBuilder.mStickyFooterShadowView;
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return this.mDrawerBuilder.mActionBarDrawerToggle;
    }

    public int getPosition(@NonNull IDrawerItem drawerItem) {
        return getPosition(drawerItem.getIdentifier());
    }

    public int getPosition(long identifier) {
        return DrawerUtils.getPositionByIdentifier(this.mDrawerBuilder, identifier);
    }

    public IDrawerItem getDrawerItem(long identifier) {
        return (IDrawerItem) getAdapter().getItem(getPosition(identifier));
    }

    public IDrawerItem getDrawerItem(Object tag) {
        return DrawerUtils.getDrawerItem(getDrawerItems(), tag);
    }

    public int getStickyFooterPosition(@NonNull IDrawerItem drawerItem) {
        return getStickyFooterPosition(drawerItem.getIdentifier());
    }

    public int getStickyFooterPosition(long identifier) {
        return DrawerUtils.getStickyFooterPositionByIdentifier(this.mDrawerBuilder, identifier);
    }

    public int getCurrentSelectedPosition() {
        return this.mDrawerBuilder.mAdapter.getSelections().size() == 0 ? -1 : ((Integer) this.mDrawerBuilder.mAdapter.getSelections().iterator().next()).intValue();
    }

    public long getCurrentSelection() {
        IDrawerItem drawerItem = this.mDrawerBuilder.getDrawerItem(getCurrentSelectedPosition());
        if (drawerItem != null) {
            return drawerItem.getIdentifier();
        }
        return -1;
    }

    public int getCurrentStickyFooterSelectedPosition() {
        return this.mDrawerBuilder.mCurrentStickyFooterSelection;
    }

    public void deselect() {
        getAdapter().deselect();
    }

    public void deselect(long identifier) {
        getAdapter().deselect(getPosition(identifier));
    }

    public boolean setSelection(long identifier) {
        return setSelectionAtPosition(getPosition(identifier), true);
    }

    public boolean setSelection(long identifier, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(identifier), fireOnClick);
    }

    public void setStickyFooterSelection(long identifier, boolean fireOnClick) {
        setStickyFooterSelectionAtPosition(getStickyFooterPosition(identifier), fireOnClick);
    }

    public boolean setSelection(@NonNull IDrawerItem drawerItem) {
        return setSelectionAtPosition(getPosition(drawerItem), true);
    }

    public boolean setSelection(@NonNull IDrawerItem drawerItem, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(drawerItem), fireOnClick);
    }

    public boolean setSelectionAtPosition(int position) {
        return setSelectionAtPosition(position, true);
    }

    public boolean setSelectionAtPosition(int position, boolean fireOnClick) {
        if (this.mDrawerBuilder.mRecyclerView != null) {
            this.mDrawerBuilder.mAdapter.deselect();
            this.mDrawerBuilder.mAdapter.select(position, false);
            if (this.mDrawerBuilder.mOnDrawerItemClickListener != null && fireOnClick && position >= 0) {
                this.mDrawerBuilder.mOnDrawerItemClickListener.onItemClick(null, position, (IDrawerItem) this.mDrawerBuilder.mAdapter.getItem(position));
            }
            this.mDrawerBuilder.resetStickyFooterSelection();
        }
        return false;
    }

    public void setStickyFooterSelectionAtPosition(int position) {
        setStickyFooterSelectionAtPosition(position, true);
    }

    public void setStickyFooterSelectionAtPosition(int position, boolean fireOnClick) {
        DrawerUtils.setStickyFooterSelection(this.mDrawerBuilder, position, Boolean.valueOf(fireOnClick));
    }

    public void updateItem(@NonNull IDrawerItem drawerItem) {
        updateItemAtPosition(drawerItem, getPosition(drawerItem));
    }

    public void updateBadge(long identifier, StringHolder badge) {
        IDrawerItem drawerItem = getDrawerItem(identifier);
        if (drawerItem instanceof Badgeable) {
            Badgeable badgeable = (Badgeable) drawerItem;
            badgeable.withBadge(badge);
            updateItem((IDrawerItem) badgeable);
        }
    }

    public void updateName(long identifier, StringHolder name) {
        IDrawerItem drawerItem = getDrawerItem(identifier);
        if (drawerItem instanceof Nameable) {
            Nameable pdi = (Nameable) drawerItem;
            pdi.withName(name);
            updateItem((IDrawerItem) pdi);
        }
    }

    public void updateIcon(long identifier, ImageHolder image) {
        IDrawerItem drawerItem = getDrawerItem(identifier);
        if (drawerItem instanceof Iconable) {
            Iconable pdi = (Iconable) drawerItem;
            pdi.withIcon(image);
            updateItem((IDrawerItem) pdi);
        }
    }

    public void updateItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (this.mDrawerBuilder.checkDrawerItem(position, false)) {
            this.mDrawerBuilder.getItemAdapter().set(position, drawerItem);
        }
    }

    public void addItem(@NonNull IDrawerItem drawerItem) {
        this.mDrawerBuilder.getItemAdapter().add(drawerItem);
    }

    public void addItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        this.mDrawerBuilder.getItemAdapter().add(position, drawerItem);
    }

    public void setItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        this.mDrawerBuilder.getItemAdapter().add(position, drawerItem);
    }

    public void removeItemByPosition(int position) {
        if (this.mDrawerBuilder.checkDrawerItem(position, false)) {
            this.mDrawerBuilder.getItemAdapter().remove(position);
        }
    }

    public void removeItem(long identifier) {
        int position = getPosition(identifier);
        if (this.mDrawerBuilder.checkDrawerItem(position, false)) {
            this.mDrawerBuilder.getItemAdapter().remove(position);
        }
    }

    public void removeItems(long... identifiers) {
        if (identifiers != null) {
            for (long identifier : identifiers) {
                removeItem(identifier);
            }
        }
    }

    public void removeAllItems() {
        this.mDrawerBuilder.getItemAdapter().clear();
    }

    public void addItems(@NonNull IDrawerItem... drawerItems) {
        this.mDrawerBuilder.getItemAdapter().add((IItem[]) drawerItems);
    }

    public void addItemsAtPosition(int position, @NonNull IDrawerItem... drawerItems) {
        this.mDrawerBuilder.getItemAdapter().add(position, (IItem[]) drawerItems);
    }

    public void setItems(@NonNull List<IDrawerItem> drawerItems) {
        setItems(drawerItems, false);
    }

    private void setItems(@NonNull List<IDrawerItem> drawerItems, boolean switchedItems) {
        if (!(this.originalDrawerItems == null || switchedItems)) {
            this.originalDrawerItems = drawerItems;
        }
        this.mDrawerBuilder.getItemAdapter().setNewList(drawerItems);
    }

    public void updateStickyFooterItem(@NonNull IDrawerItem drawerItem) {
        updateStickyFooterItemAtPosition(drawerItem, getStickyFooterPosition(drawerItem));
    }

    public void updateStickyFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (this.mDrawerBuilder.mStickyDrawerItems != null && this.mDrawerBuilder.mStickyDrawerItems.size() > position) {
            this.mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }
        DrawerUtils.rebuildStickyFooterView(this.mDrawerBuilder);
    }

    public void addStickyFooterItem(@NonNull IDrawerItem drawerItem) {
        if (this.mDrawerBuilder.mStickyDrawerItems == null) {
            this.mDrawerBuilder.mStickyDrawerItems = new ArrayList();
        }
        this.mDrawerBuilder.mStickyDrawerItems.add(drawerItem);
        DrawerUtils.rebuildStickyFooterView(this.mDrawerBuilder);
    }

    public void addStickyFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (this.mDrawerBuilder.mStickyDrawerItems == null) {
            this.mDrawerBuilder.mStickyDrawerItems = new ArrayList();
        }
        this.mDrawerBuilder.mStickyDrawerItems.add(position, drawerItem);
        DrawerUtils.rebuildStickyFooterView(this.mDrawerBuilder);
    }

    public void setStickyFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (this.mDrawerBuilder.mStickyDrawerItems != null && this.mDrawerBuilder.mStickyDrawerItems.size() > position) {
            this.mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }
        DrawerUtils.rebuildStickyFooterView(this.mDrawerBuilder);
    }

    public void removeStickyFooterItemAtPosition(int position) {
        if (this.mDrawerBuilder.mStickyDrawerItems != null && this.mDrawerBuilder.mStickyDrawerItems.size() > position) {
            this.mDrawerBuilder.mStickyDrawerItems.remove(position);
        }
        DrawerUtils.rebuildStickyFooterView(this.mDrawerBuilder);
    }

    public void removeAllStickyFooterItems() {
        if (this.mDrawerBuilder.mStickyDrawerItems != null) {
            this.mDrawerBuilder.mStickyDrawerItems.clear();
        }
        if (this.mDrawerBuilder.mStickyFooterView != null) {
            this.mDrawerBuilder.mStickyFooterView.setVisibility(8);
        }
    }

    public void setOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mDrawerBuilder.mOnDrawerItemClickListener = onDrawerItemClickListener;
    }

    public void setOnDrawerNavigationListener(OnDrawerNavigationListener onDrawerNavigationListener) {
        this.mDrawerBuilder.mOnDrawerNavigationListener = onDrawerNavigationListener;
    }

    public OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return this.mDrawerBuilder.mOnDrawerItemClickListener;
    }

    public OnDrawerNavigationListener getOnDrawerNavigationListener() {
        return this.mDrawerBuilder.mOnDrawerNavigationListener;
    }

    public void setOnDrawerItemLongClickListener(OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
        this.mDrawerBuilder.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
    }

    public OnDrawerItemLongClickListener getOnDrawerItemLongClickListener() {
        return this.mDrawerBuilder.mOnDrawerItemLongClickListener;
    }

    public boolean switchedDrawerContent() {
        return (this.originalOnDrawerItemClickListener == null && this.originalDrawerItems == null && this.originalDrawerState == null) ? false : true;
    }

    public List<IDrawerItem> getOriginalDrawerItems() {
        return this.originalDrawerItems;
    }

    public void switchDrawerContent(@NonNull OnDrawerItemClickListener onDrawerItemClickListener, OnDrawerItemLongClickListener onDrawerItemLongClickListener, @NonNull List<IDrawerItem> drawerItems, int drawerSelection) {
        if (!switchedDrawerContent()) {
            this.originalOnDrawerItemClickListener = getOnDrawerItemClickListener();
            this.originalOnDrawerItemLongClickListener = getOnDrawerItemLongClickListener();
            this.originalDrawerState = getAdapter().saveInstanceState(new Bundle());
            getAdapter().collapse(false);
            this.originalDrawerItems = getDrawerItems();
        }
        setOnDrawerItemClickListener(onDrawerItemClickListener);
        setOnDrawerItemLongClickListener(onDrawerItemLongClickListener);
        setItems(drawerItems, true);
        setSelectionAtPosition(drawerSelection, false);
        if (getStickyFooter() != null) {
            getStickyFooter().setVisibility(8);
        }
        if (getStickyFooterShadow() != null) {
            getStickyFooterShadow().setVisibility(8);
        }
    }

    public void resetDrawerContent() {
        if (switchedDrawerContent()) {
            setOnDrawerItemClickListener(this.originalOnDrawerItemClickListener);
            setOnDrawerItemLongClickListener(this.originalOnDrawerItemLongClickListener);
            setItems(this.originalDrawerItems, true);
            getAdapter().withSavedInstanceState(this.originalDrawerState);
            this.originalOnDrawerItemClickListener = null;
            this.originalOnDrawerItemLongClickListener = null;
            this.originalDrawerItems = null;
            this.originalDrawerState = null;
            this.mDrawerBuilder.mRecyclerView.smoothScrollToPosition(0);
            if (getStickyFooter() != null) {
                getStickyFooter().setVisibility(0);
            }
            if (getStickyFooterShadow() != null) {
                getStickyFooterShadow().setVisibility(0);
            }
            if (this.mDrawerBuilder.mAccountHeader != null && this.mDrawerBuilder.mAccountHeader.mAccountHeaderBuilder != null) {
                this.mDrawerBuilder.mAccountHeader.mAccountHeaderBuilder.mSelectionListShown = false;
            }
        }
    }

    public Bundle saveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return savedInstanceState;
        }
        if (this.mDrawerBuilder.mAppended) {
            savedInstanceState = this.mDrawerBuilder.mAdapter.saveInstanceState(savedInstanceState, BUNDLE_SELECTION_APPENDED);
            savedInstanceState.putInt(BUNDLE_STICKY_FOOTER_SELECTION_APPENDED, this.mDrawerBuilder.mCurrentStickyFooterSelection);
            savedInstanceState.putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED, switchedDrawerContent());
            return savedInstanceState;
        }
        savedInstanceState = this.mDrawerBuilder.mAdapter.saveInstanceState(savedInstanceState, BUNDLE_SELECTION);
        savedInstanceState.putInt(BUNDLE_STICKY_FOOTER_SELECTION, this.mDrawerBuilder.mCurrentStickyFooterSelection);
        savedInstanceState.putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED, switchedDrawerContent());
        return savedInstanceState;
    }
}
