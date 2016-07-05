package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialize.holder.ColorHolder;

public class SecondaryToggleDrawerItem extends ToggleDrawerItem {
    public int getType() {
        return R.id.material_drawer_item_secondary_toggle;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_secondary_toggle;
    }

    protected int getColor(Context ctx) {
        if (isEnabled()) {
            return ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text);
        }
        return ColorHolder.color(getDisabledTextColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
    }
}
