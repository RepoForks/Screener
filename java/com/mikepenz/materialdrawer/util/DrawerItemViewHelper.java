package com.mikepenz.materialdrawer.util;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialize.util.UIUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DrawerItemViewHelper {
    private Context mContext;
    private boolean mDivider = true;
    private ArrayList<IDrawerItem> mDrawerItems = new ArrayList();
    private OnDrawerItemClickListener mOnDrawerItemClickListener = null;

    public interface OnDrawerItemClickListener {
        void onItemClick(View view, IDrawerItem iDrawerItem);
    }

    public DrawerItemViewHelper(Context context) {
        this.mContext = context;
    }

    public DrawerItemViewHelper withDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        return this;
    }

    public DrawerItemViewHelper withDrawerItems(IDrawerItem... drawerItems) {
        Collections.addAll(this.mDrawerItems, drawerItems);
        return this;
    }

    public DrawerItemViewHelper withDivider(boolean divider) {
        this.mDivider = divider;
        return this;
    }

    public DrawerItemViewHelper withOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    public View build() {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        if (this.mDivider) {
            LinearLayout divider = new LinearLayout(this.mContext);
            divider.setLayoutParams(new LayoutParams(-1, -2));
            divider.setMinimumHeight((int) UIUtils.convertDpToPixel(1.0f, this.mContext));
            divider.setOrientation(1);
            divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this.mContext, R.attr.material_drawer_divider, R.color.material_drawer_divider));
            linearLayout.addView(divider);
        }
        Iterator it = this.mDrawerItems.iterator();
        while (it.hasNext()) {
            IDrawerItem drawerItem = (IDrawerItem) it.next();
            View view = drawerItem.generateView(this.mContext);
            view.setTag(drawerItem);
            if (drawerItem.isEnabled()) {
                view.setBackgroundResource(UIUtils.getSelectableBackgroundRes(this.mContext));
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (DrawerItemViewHelper.this.mOnDrawerItemClickListener != null) {
                            DrawerItemViewHelper.this.mOnDrawerItemClickListener.onItemClick(v, (IDrawerItem) v.getTag());
                        }
                    }
                });
            }
            linearLayout.addView(view);
        }
        return linearLayout;
    }
}
