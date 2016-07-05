package com.mikepenz.materialdrawer.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.widget.ImageView;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MiniProfileDrawerItem extends AbstractDrawerItem<MiniProfileDrawerItem, ViewHolder> implements IProfile<MiniProfileDrawerItem> {
    protected DimenHolder customHeight;
    protected ImageHolder icon;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private ImageView icon;

        public ViewHolder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
        }
    }

    public MiniProfileDrawerItem() {
        withSelectable(false);
    }

    public MiniProfileDrawerItem(ProfileDrawerItem profile) {
        this.icon = profile.icon;
        this.mEnabled = profile.mEnabled;
        withSelectable(false);
    }

    public MiniProfileDrawerItem withName(String name) {
        return null;
    }

    public StringHolder getName() {
        return null;
    }

    public MiniProfileDrawerItem withEmail(String email) {
        return null;
    }

    public StringHolder getEmail() {
        return null;
    }

    public MiniProfileDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public MiniProfileDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    public MiniProfileDrawerItem withIcon(Bitmap iconBitmap) {
        this.icon = new ImageHolder(iconBitmap);
        return this;
    }

    public MiniProfileDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    public MiniProfileDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    public MiniProfileDrawerItem withIcon(IIcon icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeightRes(@DimenRes int customHeightRes) {
        this.customHeight = DimenHolder.fromResource(customHeightRes);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeightDp(int customHeightDp) {
        this.customHeight = DimenHolder.fromDp(customHeightDp);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeightPx(int customHeightPx) {
        this.customHeight = DimenHolder.fromPixel(customHeightPx);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeight(DimenHolder customHeight) {
        this.customHeight = customHeight;
        return this;
    }

    public ImageHolder getIcon() {
        return this.icon;
    }

    public int getType() {
        return R.id.material_drawer_item_mini_profile;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_mini_profile;
    }

    public void bindView(ViewHolder viewHolder) {
        if (this.customHeight != null) {
            LayoutParams lp = (LayoutParams) viewHolder.itemView.getLayoutParams();
            lp.height = this.customHeight.asPixel(viewHolder.itemView.getContext());
            viewHolder.itemView.setLayoutParams(lp);
        }
        viewHolder.itemView.setId(hashCode());
        viewHolder.itemView.setEnabled(isEnabled());
        com.mikepenz.materialize.holder.ImageHolder.applyToOrSetInvisible(getIcon(), viewHolder.icon);
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
