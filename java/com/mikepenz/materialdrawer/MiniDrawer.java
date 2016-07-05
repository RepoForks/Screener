package com.mikepenz.materialdrawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.FastAdapter.OnClickListener;
import com.mikepenz.fastadapter.FastAdapter.OnLongClickListener;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;
import java.util.List;

public class MiniDrawer {
    public static final int ITEM = 2;
    public static final int PROFILE = 1;
    private AccountHeader mAccountHeader;
    protected FastItemAdapter<IDrawerItem> mAdapter;
    private LinearLayout mContainer;
    private ICrossfader mCrossFader;
    private Drawer mDrawer;
    private boolean mEnableProfileClick = true;
    private boolean mEnableSelectedMiniDrawerItemBackground = false;
    private boolean mInRTL = false;
    private boolean mIncludeSecondaryDrawerItems = false;
    private boolean mInnerShadow = false;
    private OnMiniDrawerItemClickListener mOnMiniDrawerItemClickListener;
    private OnLongClickListener<IDrawerItem> mOnMiniDrawerItemLongClickListener;
    private OnClickListener<IDrawerItem> mOnMiniDrawerItemOnClickListener;
    protected boolean mPositionBasedStateManagement = true;
    private RecyclerView mRecyclerView;

    public interface OnMiniDrawerItemClickListener {
        boolean onItemClick(View view, int i, IDrawerItem iDrawerItem, int i2);
    }

    public MiniDrawer withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;
        return this;
    }

    public MiniDrawer withAccountHeader(@NonNull AccountHeader accountHeader) {
        this.mAccountHeader = accountHeader;
        return this;
    }

    public MiniDrawer withCrossFader(@NonNull ICrossfader crossFader) {
        this.mCrossFader = crossFader;
        return this;
    }

    public MiniDrawer withInnerShadow(boolean innerShadow) {
        this.mInnerShadow = innerShadow;
        return this;
    }

    public MiniDrawer withInRTL(boolean inRTL) {
        this.mInRTL = inRTL;
        return this;
    }

    public MiniDrawer withPositionBasedStateManagement(boolean positionBasedStateManagement) {
        this.mPositionBasedStateManagement = positionBasedStateManagement;
        return this;
    }

    public MiniDrawer withIncludeSecondaryDrawerItems(boolean includeSecondaryDrawerItems) {
        this.mIncludeSecondaryDrawerItems = includeSecondaryDrawerItems;
        return this;
    }

    public MiniDrawer withEnableSelectedMiniDrawerItemBackground(boolean enableSelectedMiniDrawerItemBackground) {
        this.mEnableSelectedMiniDrawerItemBackground = enableSelectedMiniDrawerItemBackground;
        return this;
    }

    public MiniDrawer withEnableProfileClick(boolean enableProfileClick) {
        this.mEnableProfileClick = enableProfileClick;
        return this;
    }

    public MiniDrawer withOnMiniDrawerItemClickListener(OnMiniDrawerItemClickListener onMiniDrawerItemClickListener) {
        this.mOnMiniDrawerItemClickListener = onMiniDrawerItemClickListener;
        return this;
    }

    public MiniDrawer withOnMiniDrawerItemOnClickListener(OnClickListener<IDrawerItem> onMiniDrawerItemOnClickListener) {
        this.mOnMiniDrawerItemOnClickListener = onMiniDrawerItemOnClickListener;
        return this;
    }

    public MiniDrawer withOnMiniDrawerItemLongClickListener(OnLongClickListener<IDrawerItem> onMiniDrawerItemLongClickListener) {
        this.mOnMiniDrawerItemLongClickListener = onMiniDrawerItemLongClickListener;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public FastAdapter<IDrawerItem> getAdapter() {
        return this.mAdapter;
    }

    public ItemAdapter<IDrawerItem> getItemAdapter() {
        return this.mAdapter.getItemAdapter();
    }

    public Drawer getDrawer() {
        return this.mDrawer;
    }

    public AccountHeader getAccountHeader() {
        return this.mAccountHeader;
    }

    public ICrossfader getCrossFader() {
        return this.mCrossFader;
    }

    public OnClickListener getOnMiniDrawerItemOnClickListener() {
        return this.mOnMiniDrawerItemOnClickListener;
    }

    public OnLongClickListener getOnMiniDrawerItemLongClickListener() {
        return this.mOnMiniDrawerItemLongClickListener;
    }

    public IDrawerItem generateMiniDrawerItem(IDrawerItem drawerItem) {
        if (drawerItem instanceof SecondaryDrawerItem) {
            if (this.mIncludeSecondaryDrawerItems) {
                return new MiniDrawerItem((SecondaryDrawerItem) drawerItem).withEnableSelectedBackground(this.mEnableSelectedMiniDrawerItemBackground);
            }
            return null;
        } else if (drawerItem instanceof PrimaryDrawerItem) {
            return new MiniDrawerItem((PrimaryDrawerItem) drawerItem).withEnableSelectedBackground(this.mEnableSelectedMiniDrawerItemBackground);
        } else {
            if (!(drawerItem instanceof ProfileDrawerItem)) {
                return null;
            }
            MiniProfileDrawerItem mpdi = new MiniProfileDrawerItem((ProfileDrawerItem) drawerItem);
            mpdi.withEnabled(this.mEnableProfileClick);
            return mpdi;
        }
    }

    public int getMiniDrawerType(IDrawerItem drawerItem) {
        if (drawerItem instanceof MiniProfileDrawerItem) {
            return PROFILE;
        }
        if (drawerItem instanceof MiniDrawerItem) {
            return ITEM;
        }
        return -1;
    }

    public View build(Context ctx) {
        this.mContainer = new LinearLayout(ctx);
        if (this.mInnerShadow) {
            if (this.mInRTL) {
                this.mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_right);
            } else {
                this.mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_left);
            }
        }
        this.mRecyclerView = new RecyclerView(ctx);
        this.mContainer.addView(this.mRecyclerView, -1, -1);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.setFadingEdgeLength(0);
        this.mRecyclerView.setClipToPadding(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        this.mAdapter = new FastItemAdapter();
        this.mAdapter.withSelectable(true);
        this.mAdapter.withAllowDeselection(false);
        this.mAdapter.withPositionBasedStateManagement(this.mPositionBasedStateManagement);
        this.mRecyclerView.setAdapter(this.mAdapter);
        if (!(this.mDrawer == null || this.mDrawer.mDrawerBuilder == null || (!this.mDrawer.mDrawerBuilder.mFullscreen && !this.mDrawer.mDrawerBuilder.mTranslucentStatusBar))) {
            this.mRecyclerView.setPadding(this.mRecyclerView.getPaddingLeft(), UIUtils.getStatusBarHeight(ctx), this.mRecyclerView.getPaddingRight(), this.mRecyclerView.getPaddingBottom());
        }
        if (!(this.mDrawer == null || this.mDrawer.mDrawerBuilder == null || ((!this.mDrawer.mDrawerBuilder.mFullscreen && !this.mDrawer.mDrawerBuilder.mTranslucentNavigationBar) || ctx.getResources().getConfiguration().orientation != PROFILE))) {
            this.mRecyclerView.setPadding(this.mRecyclerView.getPaddingLeft(), this.mRecyclerView.getPaddingTop(), this.mRecyclerView.getPaddingRight(), UIUtils.getNavigationBarHeight(ctx));
        }
        createItems();
        return this.mContainer;
    }

    public void onProfileClick() {
        if (this.mCrossFader != null && this.mCrossFader.isCrossfaded()) {
            this.mCrossFader.crossfade();
        }
        if (this.mAccountHeader != null) {
            IProfile profile = this.mAccountHeader.getActiveProfile();
            if (profile instanceof IDrawerItem) {
                this.mAdapter.set(0, generateMiniDrawerItem((IDrawerItem) profile));
            }
        }
    }

    public boolean onItemClick(IDrawerItem selectedDrawerItem) {
        if (!selectedDrawerItem.isSelectable()) {
            return true;
        }
        if (this.mCrossFader != null && this.mCrossFader.isCrossfaded()) {
            this.mCrossFader.crossfade();
        }
        setSelection(selectedDrawerItem.getIdentifier());
        return false;
    }

    public void setSelection(long identifier) {
        int count = this.mAdapter.getItemCount();
        for (int i = 0; i < count; i += PROFILE) {
            IDrawerItem item = (IDrawerItem) this.mAdapter.getItem(i);
            if (item.getIdentifier() == identifier && !item.isSelected()) {
                this.mAdapter.deselect();
                this.mAdapter.select(i);
            }
        }
    }

    public void updateItem(long identifier) {
        if (this.mDrawer != null && this.mAdapter != null && this.mAdapter.getAdapterItems() != null && identifier != -1) {
            IDrawerItem drawerItem = DrawerUtils.getDrawerItem(getDrawerItems(), identifier);
            for (int i = 0; i < this.mAdapter.getAdapterItems().size(); i += PROFILE) {
                if (((IDrawerItem) this.mAdapter.getAdapterItems().get(i)).getIdentifier() == drawerItem.getIdentifier()) {
                    IDrawerItem miniDrawerItem = generateMiniDrawerItem(drawerItem);
                    if (miniDrawerItem != null) {
                        this.mAdapter.set(i, miniDrawerItem);
                    }
                }
            }
        }
    }

    public void createItems() {
        this.mAdapter.clear();
        int profileOffset = 0;
        if (this.mAccountHeader != null && this.mAccountHeader.getAccountHeaderBuilder().mProfileImagesVisible) {
            IProfile profile = this.mAccountHeader.getActiveProfile();
            if (profile instanceof IDrawerItem) {
                this.mAdapter.add(generateMiniDrawerItem((IDrawerItem) profile));
                profileOffset = PROFILE;
            }
        }
        int select = -1;
        if (!(this.mDrawer == null || getDrawerItems() == null)) {
            int length = getDrawerItems().size();
            int position = 0;
            for (int i = 0; i < length; i += PROFILE) {
                IItem miniDrawerItem = generateMiniDrawerItem((IDrawerItem) getDrawerItems().get(i));
                if (miniDrawerItem != null) {
                    if (miniDrawerItem.isSelected()) {
                        select = position;
                    }
                    this.mAdapter.add(miniDrawerItem);
                    position += PROFILE;
                }
            }
            if (select >= 0) {
                this.mAdapter.select(select + profileOffset);
            }
        }
        if (this.mOnMiniDrawerItemOnClickListener != null) {
            this.mAdapter.withOnClickListener(this.mOnMiniDrawerItemOnClickListener);
        } else {
            this.mAdapter.withOnClickListener(new OnClickListener<IDrawerItem>() {
                public boolean onClick(View v, IAdapter<IDrawerItem> iAdapter, IDrawerItem item, int position) {
                    int type = MiniDrawer.this.getMiniDrawerType(item);
                    if (MiniDrawer.this.mOnMiniDrawerItemClickListener == null || !MiniDrawer.this.mOnMiniDrawerItemClickListener.onItemClick(v, position, item, type)) {
                        if (type == MiniDrawer.ITEM) {
                            if (item.isSelectable()) {
                                if (MiniDrawer.this.mAccountHeader != null && MiniDrawer.this.mAccountHeader.isSelectionListShown()) {
                                    MiniDrawer.this.mAccountHeader.toggleSelectionList(v.getContext());
                                }
                                if (!MiniDrawer.this.mDrawer.getDrawerItem(item.getIdentifier()).isSelected()) {
                                    MiniDrawer.this.mDrawer.setSelection(item, true);
                                }
                            } else if (MiniDrawer.this.mDrawer.getOnDrawerItemClickListener() != null) {
                                MiniDrawer.this.mDrawer.getOnDrawerItemClickListener().onItemClick(v, position, DrawerUtils.getDrawerItem(MiniDrawer.this.getDrawerItems(), item.getIdentifier()));
                            }
                        } else if (type == MiniDrawer.PROFILE) {
                            if (!(MiniDrawer.this.mAccountHeader == null || MiniDrawer.this.mAccountHeader.isSelectionListShown())) {
                                MiniDrawer.this.mAccountHeader.toggleSelectionList(v.getContext());
                            }
                            if (MiniDrawer.this.mCrossFader != null) {
                                MiniDrawer.this.mCrossFader.crossfade();
                            }
                        }
                    }
                    return false;
                }
            });
        }
        this.mAdapter.withOnLongClickListener(this.mOnMiniDrawerItemLongClickListener);
        this.mRecyclerView.scrollToPosition(0);
    }

    private List<IDrawerItem> getDrawerItems() {
        return this.mDrawer.getOriginalDrawerItems() != null ? this.mDrawer.getOriginalDrawerItems() : this.mDrawer.getDrawerItems();
    }
}
