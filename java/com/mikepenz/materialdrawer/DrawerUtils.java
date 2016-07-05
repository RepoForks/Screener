package com.mikepenz.materialdrawer;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem.Position;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import java.util.List;

class DrawerUtils {
    DrawerUtils() {
    }

    public static void onFooterDrawerItemClick(DrawerBuilder drawer, IDrawerItem drawerItem, View v, Boolean fireOnClick) {
        boolean checkable = drawerItem == null || !(drawerItem instanceof Selectable) || ((Selectable) drawerItem).isSelectable();
        if (checkable) {
            drawer.resetStickyFooterSelection();
            if (VERSION.SDK_INT >= 11) {
                v.setActivated(true);
            }
            v.setSelected(true);
            drawer.getAdapter().deselect();
            if (drawer.mStickyFooterView != null && (drawer.mStickyFooterView instanceof LinearLayout)) {
                LinearLayout footer = drawer.mStickyFooterView;
                for (int i = 0; i < footer.getChildCount(); i++) {
                    if (footer.getChildAt(i) == v) {
                        drawer.mCurrentStickyFooterSelection = i;
                        break;
                    }
                }
            }
        }
        if (fireOnClick != null) {
            boolean consumed = false;
            if (fireOnClick.booleanValue() && drawer.mOnDrawerItemClickListener != null) {
                consumed = drawer.mOnDrawerItemClickListener.onItemClick(v, -1, drawerItem);
            }
            if (!consumed) {
                drawer.closeDrawerDelayed();
            }
        }
    }

    public static void setStickyFooterSelection(DrawerBuilder drawer, int position, Boolean fireOnClick) {
        if (position > -1 && drawer.mStickyFooterView != null && (drawer.mStickyFooterView instanceof LinearLayout)) {
            LinearLayout footer = drawer.mStickyFooterView;
            if (footer.getChildCount() > position && position >= 0) {
                onFooterDrawerItemClick(drawer, (IDrawerItem) footer.getChildAt(position).getTag(), footer.getChildAt(position), fireOnClick);
            }
        }
    }

    public static int getPositionByIdentifier(DrawerBuilder drawer, long identifier) {
        if (identifier >= 0) {
            for (int i = 0; i < drawer.getAdapter().getItemCount(); i++) {
                if (((IDrawerItem) drawer.getAdapter().getItem(i)).getIdentifier() == identifier) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static IDrawerItem getDrawerItem(List<IDrawerItem> drawerItems, long identifier) {
        if (identifier >= 0) {
            for (IDrawerItem drawerItem : drawerItems) {
                if (drawerItem.getIdentifier() == identifier) {
                    return drawerItem;
                }
            }
        }
        return null;
    }

    public static IDrawerItem getDrawerItem(List<IDrawerItem> drawerItems, Object tag) {
        if (tag != null) {
            for (IDrawerItem drawerItem : drawerItems) {
                if (tag.equals(drawerItem.getTag())) {
                    return drawerItem;
                }
            }
        }
        return null;
    }

    public static int getStickyFooterPositionByIdentifier(DrawerBuilder drawer, long identifier) {
        if (identifier >= 0 && drawer.mStickyFooterView != null && (drawer.mStickyFooterView instanceof LinearLayout)) {
            LinearLayout footer = drawer.mStickyFooterView;
            int shadowOffset = 0;
            for (int i = 0; i < footer.getChildCount(); i++) {
                Object o = footer.getChildAt(i).getTag();
                if (o == null && drawer.mStickyFooterDivider) {
                    shadowOffset++;
                }
                if (o != null && (o instanceof IDrawerItem) && ((IDrawerItem) o).getIdentifier() == identifier) {
                    return i - shadowOffset;
                }
            }
        }
        return -1;
    }

    public static void handleHeaderView(DrawerBuilder drawer) {
        if (drawer.mAccountHeader != null) {
            if (drawer.mAccountHeaderSticky) {
                drawer.mStickyHeaderView = drawer.mAccountHeader.getView();
            } else {
                drawer.mHeaderView = drawer.mAccountHeader.getView();
                drawer.mHeaderDivider = drawer.mAccountHeader.mAccountHeaderBuilder.mDividerBelowHeader;
                drawer.mHeaderPadding = drawer.mAccountHeader.mAccountHeaderBuilder.mPaddingBelowHeader;
            }
        }
        if (drawer.mStickyHeaderView != null) {
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            layoutParams.addRule(10, 1);
            drawer.mStickyHeaderView.setId(R.id.material_drawer_sticky_header);
            drawer.mSliderLayout.addView(drawer.mStickyHeaderView, 0, layoutParams);
            LayoutParams layoutParamsListView = (LayoutParams) drawer.mRecyclerView.getLayoutParams();
            layoutParamsListView.addRule(3, R.id.material_drawer_sticky_header);
            drawer.mRecyclerView.setLayoutParams(layoutParamsListView);
            drawer.mStickyHeaderView.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
            if (drawer.mStickyHeaderShadow) {
                if (VERSION.SDK_INT >= 21) {
                    drawer.mStickyHeaderView.setElevation(UIUtils.convertDpToPixel(4.0f, drawer.mActivity));
                } else {
                    View view = new View(drawer.mActivity);
                    view.setBackgroundResource(R.drawable.material_drawer_shadow_bottom);
                    drawer.mSliderLayout.addView(view, -1, (int) UIUtils.convertDpToPixel(4.0f, drawer.mActivity));
                    LayoutParams lps = (LayoutParams) view.getLayoutParams();
                    lps.addRule(3, R.id.material_drawer_sticky_header);
                    view.setLayoutParams(lps);
                }
            }
            drawer.mRecyclerView.setPadding(0, 0, 0, 0);
        }
        if (drawer.mHeaderView == null) {
            return;
        }
        if (drawer.mRecyclerView == null) {
            throw new RuntimeException("can't use a headerView without a recyclerView");
        }
        if (drawer.mHeaderPadding) {
            drawer.getHeaderAdapter().add(new ContainerDrawerItem().withView(drawer.mHeaderView).withHeight(drawer.mHeiderHeight).withDivider(drawer.mHeaderDivider).withViewPosition(Position.TOP));
        } else {
            drawer.getHeaderAdapter().add(new ContainerDrawerItem().withView(drawer.mHeaderView).withHeight(drawer.mHeiderHeight).withDivider(drawer.mHeaderDivider).withViewPosition(Position.NONE));
        }
        drawer.mRecyclerView.setPadding(drawer.mRecyclerView.getPaddingLeft(), 0, drawer.mRecyclerView.getPaddingRight(), drawer.mRecyclerView.getPaddingBottom());
    }

    public static void rebuildStickyFooterView(final DrawerBuilder drawer) {
        if (drawer.mSliderLayout != null) {
            if (drawer.mStickyFooterView != null) {
                drawer.mStickyFooterView.removeAllViews();
                if (drawer.mStickyFooterDivider) {
                    addStickyFooterDivider(drawer.mStickyFooterView.getContext(), drawer.mStickyFooterView);
                }
                fillStickyDrawerItemFooter(drawer, drawer.mStickyFooterView, new OnClickListener() {
                    public void onClick(View v) {
                        DrawerUtils.onFooterDrawerItemClick(drawer, (IDrawerItem) v.getTag(), v, Boolean.valueOf(true));
                    }
                });
                drawer.mStickyFooterView.setVisibility(0);
            } else {
                handleFooterView(drawer, new OnClickListener() {
                    public void onClick(View v) {
                        DrawerUtils.onFooterDrawerItemClick(drawer, (IDrawerItem) v.getTag(), v, Boolean.valueOf(true));
                    }
                });
            }
            setStickyFooterSelection(drawer, drawer.mCurrentStickyFooterSelection, Boolean.valueOf(false));
        }
    }

    public static void handleFooterView(DrawerBuilder drawer, OnClickListener onClickListener) {
        Context ctx = drawer.mSliderLayout.getContext();
        if (drawer.mStickyDrawerItems != null && drawer.mStickyDrawerItems.size() > 0) {
            drawer.mStickyFooterView = buildStickyDrawerItemFooter(ctx, drawer, onClickListener);
        }
        if (drawer.mStickyFooterView != null) {
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            layoutParams.addRule(12, 1);
            drawer.mStickyFooterView.setId(R.id.material_drawer_sticky_footer);
            drawer.mSliderLayout.addView(drawer.mStickyFooterView, layoutParams);
            if ((drawer.mTranslucentNavigationBar || drawer.mFullscreen) && VERSION.SDK_INT >= 19) {
                drawer.mStickyFooterView.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(ctx));
            }
            LayoutParams layoutParamsListView = (LayoutParams) drawer.mRecyclerView.getLayoutParams();
            layoutParamsListView.addRule(2, R.id.material_drawer_sticky_footer);
            drawer.mRecyclerView.setLayoutParams(layoutParamsListView);
            if (drawer.mStickyFooterShadow) {
                drawer.mStickyFooterShadowView = new View(ctx);
                drawer.mStickyFooterShadowView.setBackgroundResource(R.drawable.material_drawer_shadow_top);
                drawer.mSliderLayout.addView(drawer.mStickyFooterShadowView, -1, ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_sticky_footer_elevation));
                LayoutParams lps = (LayoutParams) drawer.mStickyFooterShadowView.getLayoutParams();
                lps.addRule(2, R.id.material_drawer_sticky_footer);
                drawer.mStickyFooterShadowView.setLayoutParams(lps);
            }
            drawer.mRecyclerView.setPadding(drawer.mRecyclerView.getPaddingLeft(), drawer.mRecyclerView.getPaddingTop(), drawer.mRecyclerView.getPaddingRight(), ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding));
        }
        if (drawer.mFooterView == null) {
            return;
        }
        if (drawer.mRecyclerView == null) {
            throw new RuntimeException("can't use a footerView without a recyclerView");
        } else if (drawer.mFooterDivider) {
            drawer.getFooterAdapter().add(new ContainerDrawerItem().withView(drawer.mFooterView).withViewPosition(Position.BOTTOM));
        } else {
            drawer.getFooterAdapter().add(new ContainerDrawerItem().withView(drawer.mFooterView).withViewPosition(Position.NONE));
        }
    }

    public static ViewGroup buildStickyDrawerItemFooter(Context ctx, DrawerBuilder drawer, OnClickListener onClickListener) {
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_background, R.color.material_drawer_background));
        if (drawer.mStickyFooterDivider) {
            addStickyFooterDivider(ctx, linearLayout);
        }
        fillStickyDrawerItemFooter(drawer, linearLayout, onClickListener);
        return linearLayout;
    }

    private static void addStickyFooterDivider(Context ctx, ViewGroup footerView) {
        LinearLayout divider = new LinearLayout(ctx);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(-1, -2);
        divider.setMinimumHeight((int) UIUtils.convertDpToPixel(1.0f, ctx));
        divider.setOrientation(1);
        divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
        footerView.addView(divider, dividerParams);
    }

    public static void fillStickyDrawerItemFooter(DrawerBuilder drawer, ViewGroup container, OnClickListener onClickListener) {
        for (IDrawerItem drawerItem : drawer.mStickyDrawerItems) {
            View view = drawerItem.generateView(container.getContext(), container);
            view.setTag(drawerItem);
            if (drawerItem.isEnabled()) {
                view.setOnClickListener(onClickListener);
            }
            container.addView(view);
            DrawerUIUtils.setDrawerVerticalPadding(view);
        }
        container.setPadding(0, 0, 0, 0);
    }

    public static DrawerLayout.LayoutParams processDrawerLayoutParams(DrawerBuilder drawer, DrawerLayout.LayoutParams params) {
        if (params != null) {
            if (drawer.mDrawerGravity != null && (drawer.mDrawerGravity.intValue() == 5 || drawer.mDrawerGravity.intValue() == 8388613)) {
                params.rightMargin = 0;
                if (VERSION.SDK_INT >= 17) {
                    params.setMarginEnd(0);
                }
                params.leftMargin = drawer.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_margin);
                if (VERSION.SDK_INT >= 17) {
                    params.setMarginEnd(drawer.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_margin));
                }
            }
            if (drawer.mDrawerWidth > -1) {
                params.width = drawer.mDrawerWidth;
            } else {
                params.width = DrawerUIUtils.getOptimalDrawerWidth(drawer.mActivity);
            }
        }
        return params;
    }
}
