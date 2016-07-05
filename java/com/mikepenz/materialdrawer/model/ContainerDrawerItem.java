package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialize.util.UIUtils;

public class ContainerDrawerItem extends AbstractDrawerItem<ContainerDrawerItem, ViewHolder> {
    private boolean mDivider = true;
    private DimenHolder mHeight;
    private View mView;
    private Position mViewPosition = Position.TOP;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public enum Position {
        TOP,
        BOTTOM,
        NONE
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private View view;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    public ContainerDrawerItem withHeight(DimenHolder height) {
        this.mHeight = height;
        return this;
    }

    public DimenHolder getHeight() {
        return this.mHeight;
    }

    public ContainerDrawerItem withView(View view) {
        this.mView = view;
        return this;
    }

    public View getView() {
        return this.mView;
    }

    public ContainerDrawerItem withViewPosition(Position position) {
        this.mViewPosition = position;
        return this;
    }

    public ContainerDrawerItem withDivider(boolean divider) {
        this.mDivider = divider;
        return this;
    }

    public Position getViewPosition() {
        return this.mViewPosition;
    }

    public int getType() {
        return R.id.material_drawer_item_container;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_container;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemView.setId(hashCode());
        viewHolder.view.setEnabled(false);
        if (this.mView.getParent() != null) {
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
        }
        if (this.mHeight != null) {
            LayoutParams lp = (LayoutParams) viewHolder.view.getLayoutParams();
            lp.height = this.mHeight.asPixel(ctx);
            viewHolder.view.setLayoutParams(lp);
        }
        ((ViewGroup) viewHolder.view).removeAllViews();
        int dividerHeight = 0;
        if (this.mDivider) {
            dividerHeight = 1;
        }
        View divider = new View(ctx);
        divider.setMinimumHeight(dividerHeight);
        divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, (int) UIUtils.convertDpToPixel((float) dividerHeight, ctx));
        if (this.mViewPosition == Position.TOP) {
            ((ViewGroup) viewHolder.view).addView(this.mView, -1, -2);
            layoutParams.bottomMargin = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
            ((ViewGroup) viewHolder.view).addView(divider, layoutParams);
        } else if (this.mViewPosition == Position.BOTTOM) {
            layoutParams.topMargin = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
            ((ViewGroup) viewHolder.view).addView(divider, layoutParams);
            ((ViewGroup) viewHolder.view).addView(this.mView);
        } else {
            ((ViewGroup) viewHolder.view).addView(this.mView);
        }
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
