package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.view.View;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont.Icon;
import com.mikepenz.materialize.util.UIUtils;

@SuppressLint({"InlinedApi"})
public class DrawerUIUtils {
    public static ColorStateList getTextColorStateList(int text_color, int selected_text_color) {
        r1 = new int[2][];
        r1[0] = new int[]{16842913};
        r1[1] = new int[0];
        return new ColorStateList(r1, new int[]{selected_text_color, text_color});
    }

    public static StateListDrawable getIconStateList(Drawable icon, Drawable selectedIcon) {
        StateListDrawable iconStateListDrawable = new StateListDrawable();
        iconStateListDrawable.addState(new int[]{16842913}, selectedIcon);
        iconStateListDrawable.addState(new int[0], icon);
        return iconStateListDrawable;
    }

    public static StateListDrawable getDrawerItemBackground(int selected_color) {
        ColorDrawable clrActive = new ColorDrawable(selected_color);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{16842913}, clrActive);
        return states;
    }

    public static int getOptimalDrawerWidth(Context context) {
        return Math.min(UIUtils.getScreenWidth(context) - UIUtils.getActionBarHeight(context), context.getResources().getDimensionPixelSize(R.dimen.material_drawer_width));
    }

    public static Drawable getPlaceHolder(Context ctx) {
        return new IconicsDrawable(ctx, Icon.mdf_person).colorRes(R.color.accent).backgroundColorRes(R.color.primary).sizeDp(56).paddingDp(16);
    }

    public static void setDrawerVerticalPadding(View v) {
        int verticalPadding = v.getContext().getResources().getDimensionPixelSize(R.dimen.material_drawer_vertical_padding);
        v.setPadding(verticalPadding, 0, verticalPadding, 0);
    }

    public static void setDrawerVerticalPadding(View v, int level) {
        int verticalPadding = v.getContext().getResources().getDimensionPixelSize(R.dimen.material_drawer_vertical_padding);
        if (VERSION.SDK_INT >= 17) {
            v.setPaddingRelative(verticalPadding * level, 0, verticalPadding, 0);
        } else {
            v.setPadding(verticalPadding * level, 0, verticalPadding, 0);
        }
    }
}
