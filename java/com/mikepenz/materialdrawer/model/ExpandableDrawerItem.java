package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont.Icon;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class ExpandableDrawerItem extends BasePrimaryDrawerItem<ExpandableDrawerItem, ViewHolder> {
    protected ColorHolder arrowColor;
    private OnDrawerItemClickListener mOnArrowDrawerItemClickListener = new OnDrawerItemClickListener() {
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if ((drawerItem instanceof AbstractDrawerItem) && drawerItem.isEnabled() && ((AbstractDrawerItem) drawerItem).getSubItems() != null) {
                if (((AbstractDrawerItem) drawerItem).isExpanded()) {
                    ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(180.0f).start();
                } else {
                    ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(0.0f).start();
                }
            }
            if (ExpandableDrawerItem.this.mOnDrawerItemClickListener == null || !ExpandableDrawerItem.this.mOnDrawerItemClickListener.onItemClick(view, position, drawerItem)) {
                return false;
            }
            return true;
        }
    };
    private OnDrawerItemClickListener mOnDrawerItemClickListener;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        public IconicsImageView arrow;

        public ViewHolder(View view) {
            super(view);
            this.arrow = (IconicsImageView) view.findViewById(R.id.material_drawer_arrow);
            this.arrow.setIcon(new IconicsDrawable(view.getContext(), Icon.mdf_expand_more).sizeDp(16).paddingDp(2).color(-16777216));
        }
    }

    public ExpandableDrawerItem withArrowColor(@ColorInt int arrowColor) {
        this.arrowColor = ColorHolder.fromColor(arrowColor);
        return this;
    }

    public ExpandableDrawerItem withArrowColorRes(@ColorRes int arrowColorRes) {
        this.arrowColor = ColorHolder.fromColorRes(arrowColorRes);
        return this;
    }

    public int getType() {
        return R.id.material_drawer_item_expandable;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_expandable;
    }

    public ExpandableDrawerItem withOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    public OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return this.mOnArrowDrawerItemClickListener;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        bindViewHelper(viewHolder);
        viewHolder.arrow.setColor(this.arrowColor != null ? this.arrowColor.color(ctx) : getIconColor(ctx));
        viewHolder.arrow.clearAnimation();
        if (isExpanded()) {
            ViewCompat.setRotation(viewHolder.arrow, 180.0f);
        } else {
            ViewCompat.setRotation(viewHolder.arrow, 0.0f);
        }
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
