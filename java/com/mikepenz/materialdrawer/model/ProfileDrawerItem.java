package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.Pair;
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
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader.Tags;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public class ProfileDrawerItem extends AbstractDrawerItem<ProfileDrawerItem, ViewHolder> implements IProfile<ProfileDrawerItem>, Tagable<ProfileDrawerItem>, Typefaceable<ProfileDrawerItem> {
    protected Pair<Integer, ColorStateList> colorStateList;
    protected ColorHolder disabledTextColor;
    protected StringHolder email;
    protected ImageHolder icon;
    protected StringHolder name;
    protected boolean nameShown = false;
    protected ColorHolder selectedColor;
    protected ColorHolder selectedTextColor;
    protected ColorHolder textColor;
    protected Typeface typeface = null;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private TextView email;
        private TextView name;
        private ImageView profileIcon;
        private View view;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.profileIcon = (ImageView) view.findViewById(R.id.material_drawer_profileIcon);
            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
            this.email = (TextView) view.findViewById(R.id.material_drawer_email);
        }
    }

    public ProfileDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public ProfileDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    public ProfileDrawerItem withIcon(Bitmap iconBitmap) {
        this.icon = new ImageHolder(iconBitmap);
        return this;
    }

    public ProfileDrawerItem withIcon(IIcon icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public ProfileDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    public ProfileDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    public ProfileDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public ProfileDrawerItem withEmail(String email) {
        this.email = new StringHolder(email);
        return this;
    }

    public ProfileDrawerItem withNameShown(boolean nameShown) {
        this.nameShown = nameShown;
        return this;
    }

    public ProfileDrawerItem withSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public ProfileDrawerItem withSelectedColorRes(@ColorRes int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public ProfileDrawerItem withTextColor(@ColorInt int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public ProfileDrawerItem withTextColorRes(@ColorRes int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public ProfileDrawerItem withSelectedTextColor(@ColorInt int selectedTextColor) {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor);
        return this;
    }

    public ProfileDrawerItem withSelectedTextColorRes(@ColorRes int selectedColorRes) {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public ProfileDrawerItem withDisabledTextColor(@ColorInt int disabledTextColor) {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor);
        return this;
    }

    public ProfileDrawerItem withDisabledTextColorRes(@ColorRes int disabledTextColorRes) {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes);
        return this;
    }

    public ProfileDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public boolean isNameShown() {
        return this.nameShown;
    }

    public ColorHolder getSelectedColor() {
        return this.selectedColor;
    }

    public ColorHolder getTextColor() {
        return this.textColor;
    }

    public ColorHolder getSelectedTextColor() {
        return this.selectedTextColor;
    }

    public ColorHolder getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    public ImageHolder getIcon() {
        return this.icon;
    }

    public StringHolder getName() {
        return this.name;
    }

    public StringHolder getEmail() {
        return this.email;
    }

    public int getType() {
        return R.id.material_drawer_item_profile;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_profile;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemView.setId(hashCode());
        viewHolder.itemView.setEnabled(isEnabled());
        viewHolder.itemView.setSelected(isSelected());
        int selectedColor = com.mikepenz.materialize.holder.ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
        int color = getColor(ctx);
        int selectedTextColor = getSelectedTextColor(ctx);
        UIUtils.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, selectedColor, true));
        if (this.nameShown) {
            viewHolder.name.setVisibility(0);
            com.mikepenz.materialize.holder.StringHolder.applyTo(getName(), viewHolder.name);
        } else {
            viewHolder.name.setVisibility(8);
        }
        if (this.nameShown || getEmail() != null || getName() == null) {
            com.mikepenz.materialize.holder.StringHolder.applyTo(getEmail(), viewHolder.email);
        } else {
            com.mikepenz.materialize.holder.StringHolder.applyTo(getName(), viewHolder.email);
        }
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.email.setTypeface(getTypeface());
        }
        if (this.nameShown) {
            viewHolder.name.setTextColor(getTextColorStateList(color, selectedTextColor));
        }
        viewHolder.email.setTextColor(getTextColorStateList(color, selectedTextColor));
        DrawerImageLoader.getInstance().cancelImage(viewHolder.profileIcon);
        com.mikepenz.materialize.holder.ImageHolder.applyToOrSetInvisible(getIcon(), viewHolder.profileIcon, Tags.PROFILE_DRAWER_ITEM.name());
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.view);
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }

    protected int getColor(Context ctx) {
        if (isEnabled()) {
            return com.mikepenz.materialize.holder.ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        }
        return com.mikepenz.materialize.holder.ColorHolder.color(getDisabledTextColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
    }

    protected int getSelectedTextColor(Context ctx) {
        return com.mikepenz.materialize.holder.ColorHolder.color(getSelectedTextColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    protected ColorStateList getTextColorStateList(@ColorInt int color, @ColorInt int selectedTextColor) {
        if (this.colorStateList == null || color + selectedTextColor != ((Integer) this.colorStateList.first).intValue()) {
            this.colorStateList = new Pair(Integer.valueOf(color + selectedTextColor), DrawerUIUtils.getTextColorStateList(color, selectedTextColor));
        }
        return (ColorStateList) this.colorStateList.second;
    }
}
