package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialize.util.UIUtils;

public class SectionDrawerItem extends AbstractDrawerItem<SectionDrawerItem, ViewHolder> implements Nameable<SectionDrawerItem>, Typefaceable<SectionDrawerItem> {
    private boolean divider = true;
    private StringHolder name;
    private ColorHolder textColor;
    private Typeface typeface = null;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private View divider;
        private TextView name;
        private View view;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(R.id.material_drawer_divider);
            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
        }
    }

    public SectionDrawerItem withName(StringHolder name) {
        this.name = name;
        return this;
    }

    public SectionDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public SectionDrawerItem withName(@StringRes int nameRes) {
        this.name = new StringHolder(nameRes);
        return this;
    }

    public SectionDrawerItem withDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    public SectionDrawerItem withTextColor(int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public SectionDrawerItem withTextColorRes(int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public SectionDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public boolean hasDivider() {
        return this.divider;
    }

    public ColorHolder getTextColor() {
        return this.textColor;
    }

    public StringHolder getName() {
        return this.name;
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean isSelected() {
        return false;
    }

    public int getType() {
        return R.id.material_drawer_item_section;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_section;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemView.setId(hashCode());
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);
        viewHolder.name.setTextColor(com.mikepenz.materialize.holder.ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text));
        com.mikepenz.materialize.holder.StringHolder.applyTo(getName(), viewHolder.name);
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
        }
        if (hasDivider()) {
            viewHolder.divider.setVisibility(0);
        } else {
            viewHolder.divider.setVisibility(8);
        }
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
