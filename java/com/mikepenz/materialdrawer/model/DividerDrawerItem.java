package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialize.util.UIUtils;

public class DividerDrawerItem extends AbstractDrawerItem<DividerDrawerItem, ViewHolder> {

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private View divider;
        private View view;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(R.id.material_drawer_divider);
        }
    }

    public int getType() {
        return R.id.material_drawer_item_divider;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_divider;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemView.setId(hashCode());
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);
        viewHolder.view.setMinimumHeight(1);
        ViewCompat.setImportantForAccessibility(viewHolder.view, 2);
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
