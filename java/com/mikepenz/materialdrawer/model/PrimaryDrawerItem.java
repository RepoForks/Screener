package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable;

public class PrimaryDrawerItem extends BasePrimaryDrawerItem<PrimaryDrawerItem, ViewHolder> implements ColorfulBadgeable<PrimaryDrawerItem> {
    protected StringHolder mBadge;
    protected BadgeStyle mBadgeStyle = new BadgeStyle();

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        private TextView badge;
        private View badgeContainer;

        public ViewHolder(View view) {
            super(view);
            this.badgeContainer = view.findViewById(R.id.material_drawer_badge_container);
            this.badge = (TextView) view.findViewById(R.id.material_drawer_badge);
        }
    }

    public PrimaryDrawerItem withBadge(StringHolder badge) {
        this.mBadge = badge;
        return this;
    }

    public PrimaryDrawerItem withBadge(String badge) {
        this.mBadge = new StringHolder(badge);
        return this;
    }

    public PrimaryDrawerItem withBadge(@StringRes int badgeRes) {
        this.mBadge = new StringHolder(badgeRes);
        return this;
    }

    public PrimaryDrawerItem withBadgeStyle(BadgeStyle badgeStyle) {
        this.mBadgeStyle = badgeStyle;
        return this;
    }

    public StringHolder getBadge() {
        return this.mBadge;
    }

    public BadgeStyle getBadgeStyle() {
        return this.mBadgeStyle;
    }

    public int getType() {
        return R.id.material_drawer_item_primary;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_primary;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        bindViewHelper(viewHolder);
        if (com.mikepenz.materialize.holder.StringHolder.applyToOrHide(this.mBadge, viewHolder.badge)) {
            this.mBadgeStyle.style(viewHolder.badge, getTextColorStateList(getColor(ctx), getSelectedTextColor(ctx)));
            viewHolder.badgeContainer.setVisibility(0);
        } else {
            viewHolder.badgeContainer.setVisibility(8);
        }
        if (getTypeface() != null) {
            viewHolder.badge.setTypeface(getTypeface());
        }
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
