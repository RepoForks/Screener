package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialize.holder.ImageHolder;
import com.mikepenz.materialize.util.UIUtils;

public class MiniDrawerItem extends BaseDrawerItem<MiniDrawerItem, ViewHolder> {
    private StringHolder mBadge;
    private BadgeStyle mBadgeStyle = new BadgeStyle();
    protected DimenHolder mCustomHeight;
    private boolean mEnableSelectedBackground = false;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private TextView badge;
        private ImageView icon;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
            this.badge = (TextView) view.findViewById(R.id.material_drawer_badge);
        }
    }

    public MiniDrawerItem(PrimaryDrawerItem primaryDrawerItem) {
        this.mIdentifier = primaryDrawerItem.mIdentifier;
        this.mTag = primaryDrawerItem.mTag;
        this.mBadge = primaryDrawerItem.mBadge;
        this.mBadgeStyle = primaryDrawerItem.mBadgeStyle;
        this.mEnabled = primaryDrawerItem.mEnabled;
        this.mSelectable = primaryDrawerItem.mSelectable;
        this.mSelected = primaryDrawerItem.mSelected;
        this.icon = primaryDrawerItem.icon;
        this.selectedIcon = primaryDrawerItem.selectedIcon;
        this.iconTinted = primaryDrawerItem.iconTinted;
        this.selectedColor = primaryDrawerItem.selectedColor;
        this.iconColor = primaryDrawerItem.iconColor;
        this.selectedIconColor = primaryDrawerItem.selectedIconColor;
        this.disabledIconColor = primaryDrawerItem.disabledIconColor;
    }

    public MiniDrawerItem(SecondaryDrawerItem secondaryDrawerItem) {
        this.mIdentifier = secondaryDrawerItem.mIdentifier;
        this.mTag = secondaryDrawerItem.mTag;
        this.mBadge = secondaryDrawerItem.mBadge;
        this.mBadgeStyle = secondaryDrawerItem.mBadgeStyle;
        this.mEnabled = secondaryDrawerItem.mEnabled;
        this.mSelectable = secondaryDrawerItem.mSelectable;
        this.mSelected = secondaryDrawerItem.mSelected;
        this.icon = secondaryDrawerItem.icon;
        this.selectedIcon = secondaryDrawerItem.selectedIcon;
        this.iconTinted = secondaryDrawerItem.iconTinted;
        this.selectedColor = secondaryDrawerItem.selectedColor;
        this.iconColor = secondaryDrawerItem.iconColor;
        this.selectedIconColor = secondaryDrawerItem.selectedIconColor;
        this.disabledIconColor = secondaryDrawerItem.disabledIconColor;
    }

    public MiniDrawerItem withCustomHeightRes(@DimenRes int customHeightRes) {
        this.mCustomHeight = DimenHolder.fromResource(customHeightRes);
        return this;
    }

    public MiniDrawerItem withCustomHeightDp(int customHeightDp) {
        this.mCustomHeight = DimenHolder.fromDp(customHeightDp);
        return this;
    }

    public MiniDrawerItem withCustomHeightPx(int customHeightPx) {
        this.mCustomHeight = DimenHolder.fromPixel(customHeightPx);
        return this;
    }

    public MiniDrawerItem withCustomHeight(DimenHolder customHeight) {
        this.mCustomHeight = customHeight;
        return this;
    }

    public MiniDrawerItem withEnableSelectedBackground(boolean enableSelectedBackground) {
        this.mEnableSelectedBackground = enableSelectedBackground;
        return this;
    }

    public int getType() {
        return R.id.material_drawer_item_mini;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_mini;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        if (this.mCustomHeight != null) {
            LayoutParams lp = (LayoutParams) viewHolder.itemView.getLayoutParams();
            lp.height = this.mCustomHeight.asPixel(ctx);
            viewHolder.itemView.setLayoutParams(lp);
        }
        viewHolder.itemView.setId(hashCode());
        viewHolder.itemView.setEnabled(isEnabled());
        viewHolder.itemView.setSelected(isSelected());
        viewHolder.itemView.setTag(this);
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);
        if (this.mEnableSelectedBackground) {
            UIUtils.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, getSelectedColor(ctx), true));
        }
        if (com.mikepenz.materialize.holder.StringHolder.applyToOrHide(this.mBadge, viewHolder.badge)) {
            this.mBadgeStyle.style(viewHolder.badge);
        }
        ImageHolder.applyMultiIconTo(com.mikepenz.materialdrawer.holder.ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1), iconColor, com.mikepenz.materialdrawer.holder.ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1), selectedIconColor, isIconTinted(), viewHolder.icon);
        int verticalPadding = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
        int topBottomPadding = ctx.getResources().getDimensionPixelSize(R.dimen.material_mini_drawer_item_padding);
        viewHolder.itemView.setPadding(verticalPadding, topBottomPadding, verticalPadding, topBottomPadding);
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
