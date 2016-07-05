package com.afollestad.materialdialogs.simplelist;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.commons.R;
import com.afollestad.materialdialogs.internal.MDAdapter;

public class MaterialSimpleListAdapter extends ArrayAdapter<MaterialSimpleListItem> implements MDAdapter {
    private MaterialDialog dialog;

    public MaterialSimpleListAdapter(Context context) {
        super(context, R.layout.md_simplelist_item, 16908310);
    }

    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    public boolean hasStableIds() {
        return true;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int index, View convertView, ViewGroup parent) {
        View view = super.getView(index, convertView, parent);
        if (this.dialog != null) {
            MaterialSimpleListItem item = (MaterialSimpleListItem) getItem(index);
            ImageView ic = (ImageView) view.findViewById(16908294);
            if (item.getIcon() != null) {
                ic.setImageDrawable(item.getIcon());
                ic.setPadding(item.getIconPadding(), item.getIconPadding(), item.getIconPadding(), item.getIconPadding());
                ic.getBackground().setColorFilter(item.getBackgroundColor(), Mode.SRC_ATOP);
            } else {
                ic.setVisibility(8);
            }
            TextView tv = (TextView) view.findViewById(16908310);
            tv.setTextColor(this.dialog.getBuilder().getItemColor());
            tv.setText(item.getContent());
            this.dialog.setTypeface(tv, this.dialog.getBuilder().getRegularFont());
        }
        return view;
    }
}
