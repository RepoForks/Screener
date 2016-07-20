package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public abstract class BasePrimaryDrawerItem<T, VH extends BaseViewHolder> extends BaseDrawerItem<T, VH> {
    private StringHolder description;
    private ColorHolder descriptionTextColor;

    public T withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public T withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    public T withDescriptionTextColor(@ColorInt int color) {
        this.descriptionTextColor = ColorHolder.fromColor(color);
        return this;
    }

    public T withDescriptionTextColorRes(@ColorRes int colorRes) {
        this.descriptionTextColor = ColorHolder.fromColorRes(colorRes);
        return this;
    }

    public StringHolder getDescription() {
        return this.description;
    }

    public ColorHolder getDescriptionTextColor() {
        return this.descriptionTextColor;
    }

    protected void bindViewHelper(BaseViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemView.setId(hashCode());
        viewHolder.itemView.setSelected(isSelected());
        viewHolder.itemView.setEnabled(isEnabled());
        viewHolder.itemView.setTag(this);
        int selectedColor = getSelectedColor(ctx);
        ColorStateList selectedTextColor = getTextColorStateList(getColor(ctx), getSelectedTextColor(ctx));
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);
        UIUtils.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, selectedColor, true));
        com.mikepenz.materialize.holder.StringHolder.applyTo(getName(), viewHolder.name);
        com.mikepenz.materialize.holder.StringHolder.applyToOrHide(getDescription(), viewHolder.description);
        viewHolder.name.setTextColor(selectedTextColor);
        com.mikepenz.materialize.holder.ColorHolder.applyToOr(getDescriptionTextColor(), viewHolder.description, selectedTextColor);
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.description.setTypeface(getTypeface());
        }
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        if (icon != null) {
            com.mikepenz.materialize.holder.ImageHolder.applyMultiIconTo(icon, iconColor, ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1), selectedIconColor, isIconTinted(), viewHolder.icon);
        } else {
            ImageHolder.applyDecidedIconOrSetGone(getIcon(), viewHolder.icon, iconColor, isIconTinted(), 1);
        }
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.view, this.level);
    }
}
