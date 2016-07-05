package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Pair;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

public abstract class BaseDrawerItem<T, VH extends ViewHolder> extends AbstractDrawerItem<T, VH> implements Nameable<T>, Iconable<T>, Tagable<T>, Typefaceable<T> {
    protected Pair<Integer, ColorStateList> colorStateList;
    protected ColorHolder disabledIconColor;
    protected ColorHolder disabledTextColor;
    protected ImageHolder icon;
    protected ColorHolder iconColor;
    protected boolean iconTinted = false;
    protected int level = 1;
    protected StringHolder name;
    protected ColorHolder selectedColor;
    protected ImageHolder selectedIcon;
    protected ColorHolder selectedIconColor;
    protected ColorHolder selectedTextColor;
    protected ColorHolder textColor;
    protected Typeface typeface = null;

    public T withIcon(ImageHolder icon) {
        this.icon = icon;
        return this;
    }

    public T withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public T withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    public T withSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = new ImageHolder(selectedIcon);
        return this;
    }

    public T withSelectedIcon(@DrawableRes int selectedIconRes) {
        this.selectedIcon = new ImageHolder(selectedIconRes);
        return this;
    }

    public T withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);
        if (VERSION.SDK_INT >= 21) {
            this.selectedIcon = new ImageHolder(iicon);
        } else {
            withIconTintingEnabled(true);
        }
        return this;
    }

    public T withName(StringHolder name) {
        this.name = name;
        return this;
    }

    public T withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public T withName(@StringRes int nameRes) {
        this.name = new StringHolder(nameRes);
        return this;
    }

    public T withSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public T withSelectedColorRes(@ColorRes int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public T withTextColor(@ColorInt int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public T withTextColorRes(@ColorRes int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public T withSelectedTextColor(@ColorInt int selectedTextColor) {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor);
        return this;
    }

    public T withSelectedTextColorRes(@ColorRes int selectedColorRes) {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public T withDisabledTextColor(@ColorInt int disabledTextColor) {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor);
        return this;
    }

    public T withDisabledTextColorRes(@ColorRes int disabledTextColorRes) {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes);
        return this;
    }

    public T withIconColor(@ColorInt int iconColor) {
        this.iconColor = ColorHolder.fromColor(iconColor);
        return this;
    }

    public T withIconColorRes(@ColorRes int iconColorRes) {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes);
        return this;
    }

    public T withSelectedIconColor(@ColorInt int selectedIconColor) {
        this.selectedIconColor = ColorHolder.fromColor(selectedIconColor);
        return this;
    }

    public T withSelectedIconColorRes(@ColorRes int selectedColorRes) {
        this.selectedIconColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public T withDisabledIconColor(@ColorInt int disabledIconColor) {
        this.disabledIconColor = ColorHolder.fromColor(disabledIconColor);
        return this;
    }

    public T withDisabledIconColorRes(@ColorRes int disabledIconColorRes) {
        this.disabledIconColor = ColorHolder.fromColorRes(disabledIconColorRes);
        return this;
    }

    public T withIconTintingEnabled(boolean iconTintingEnabled) {
        this.iconTinted = iconTintingEnabled;
        return this;
    }

    @Deprecated
    public T withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return this;
    }

    @Deprecated
    public T withTintSelectedIcon(boolean iconTinted) {
        return withIconTintingEnabled(iconTinted);
    }

    public T withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public T withLevel(int level) {
        this.level = level;
        return this;
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

    public boolean isIconTinted() {
        return this.iconTinted;
    }

    public ImageHolder getIcon() {
        return this.icon;
    }

    public ImageHolder getSelectedIcon() {
        return this.selectedIcon;
    }

    public StringHolder getName() {
        return this.name;
    }

    public ColorHolder getDisabledIconColor() {
        return this.disabledIconColor;
    }

    public ColorHolder getSelectedIconColor() {
        return this.selectedIconColor;
    }

    public ColorHolder getIconColor() {
        return this.iconColor;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    public int getLevel() {
        return this.level;
    }

    protected int getSelectedColor(Context ctx) {
        return com.mikepenz.materialize.holder.ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
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

    public int getIconColor(Context ctx) {
        if (isEnabled()) {
            return com.mikepenz.materialize.holder.ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        }
        return com.mikepenz.materialize.holder.ColorHolder.color(getDisabledIconColor(), ctx, R.attr.material_drawer_hint_icon, R.color.material_drawer_hint_icon);
    }

    protected int getSelectedIconColor(Context ctx) {
        return com.mikepenz.materialize.holder.ColorHolder.color(getSelectedIconColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    protected ColorStateList getTextColorStateList(@ColorInt int color, @ColorInt int selectedTextColor) {
        if (this.colorStateList == null || color + selectedTextColor != ((Integer) this.colorStateList.first).intValue()) {
            this.colorStateList = new Pair(Integer.valueOf(color + selectedTextColor), DrawerUIUtils.getTextColorStateList(color, selectedTextColor));
        }
        return (ColorStateList) this.colorStateList.second;
    }
}
