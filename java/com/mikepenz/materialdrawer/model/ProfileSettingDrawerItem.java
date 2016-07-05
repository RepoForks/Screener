package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public class ProfileSettingDrawerItem extends AbstractDrawerItem<ProfileSettingDrawerItem, ViewHolder> implements IProfile<ProfileSettingDrawerItem>, Tagable<ProfileSettingDrawerItem>, Typefaceable<ProfileSettingDrawerItem> {
    private StringHolder email;
    private ImageHolder icon;
    private ColorHolder iconColor;
    private boolean iconTinted = false;
    private StringHolder name;
    private boolean selectable = false;
    private ColorHolder selectedColor;
    private ColorHolder textColor;
    private Typeface typeface = null;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private View view;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
        }
    }

    public ProfileSettingDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public ProfileSettingDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    public ProfileSettingDrawerItem withIcon(Bitmap icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public ProfileSettingDrawerItem withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);
        return this;
    }

    public ProfileSettingDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    public ProfileSettingDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    public ProfileSettingDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public ProfileSettingDrawerItem withDescription(String description) {
        this.email = new StringHolder(description);
        return this;
    }

    public ProfileSettingDrawerItem withEmail(String email) {
        this.email = new StringHolder(email);
        return this;
    }

    public ProfileSettingDrawerItem withSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public ProfileSettingDrawerItem withSelectedColorRes(@ColorRes int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public ProfileSettingDrawerItem withTextColor(@ColorInt int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public ProfileSettingDrawerItem withTextColorRes(@ColorRes int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public ProfileSettingDrawerItem withIconColor(@ColorInt int iconColor) {
        this.iconColor = ColorHolder.fromColor(iconColor);
        return this;
    }

    public ProfileSettingDrawerItem withIconColorRes(@ColorRes int iconColorRes) {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes);
        return this;
    }

    public ProfileSettingDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public ProfileSettingDrawerItem withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return this;
    }

    public ColorHolder getSelectedColor() {
        return this.selectedColor;
    }

    public ColorHolder getTextColor() {
        return this.textColor;
    }

    public ColorHolder getIconColor() {
        return this.iconColor;
    }

    public ImageHolder getIcon() {
        return this.icon;
    }

    public boolean isIconTinted() {
        return this.iconTinted;
    }

    public void setIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    public StringHolder getName() {
        return this.name;
    }

    public StringHolder getEmail() {
        return this.email;
    }

    public StringHolder getDescription() {
        return this.email;
    }

    public void setDescription(String description) {
        this.email = new StringHolder(description);
    }

    public boolean isSelectable() {
        return this.selectable;
    }

    public ProfileSettingDrawerItem withSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    public int getType() {
        return R.id.material_drawer_item_profile_setting;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_profile_setting;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemView.setId(hashCode());
        viewHolder.itemView.setEnabled(isEnabled());
        viewHolder.itemView.setSelected(isSelected());
        int selectedColor = com.mikepenz.materialize.holder.ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
        int color = com.mikepenz.materialize.holder.ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        int iconColor = com.mikepenz.materialize.holder.ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        UIUtils.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, selectedColor, true));
        com.mikepenz.materialize.holder.StringHolder.applyTo(getName(), viewHolder.name);
        viewHolder.name.setTextColor(color);
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
        }
        ImageHolder.applyDecidedIconOrSetGone(this.icon, viewHolder.icon, iconColor, isIconTinted(), 2);
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.view);
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
