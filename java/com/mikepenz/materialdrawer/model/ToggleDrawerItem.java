package com.mikepenz.materialdrawer.model;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class ToggleDrawerItem extends BasePrimaryDrawerItem<ToggleDrawerItem, ViewHolder> {
    private boolean checked = false;
    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (ToggleDrawerItem.this.isEnabled()) {
                ToggleDrawerItem.this.checked = isChecked;
                if (ToggleDrawerItem.this.getOnCheckedChangeListener() != null) {
                    ToggleDrawerItem.this.getOnCheckedChangeListener().onCheckedChanged(ToggleDrawerItem.this, buttonView, isChecked);
                    return;
                }
                return;
            }
            buttonView.setOnCheckedChangeListener(null);
            buttonView.setChecked(!isChecked);
            buttonView.setOnCheckedChangeListener(ToggleDrawerItem.this.checkedChangeListener);
        }
    };
    private com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener onCheckedChangeListener = null;
    private boolean toggleEnabled = true;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        private ToggleButton toggle;

        private ViewHolder(View view) {
            super(view);
            this.toggle = (ToggleButton) view.findViewById(R.id.material_drawer_toggle);
        }
    }

    public ToggleDrawerItem withChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public ToggleDrawerItem withToggleEnabled(boolean toggleEnabled) {
        this.toggleEnabled = toggleEnabled;
        return this;
    }

    public ToggleDrawerItem withOnCheckedChangeListener(com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isToggleEnabled() {
        return this.toggleEnabled;
    }

    public void setToggleEnabled(boolean toggleEnabled) {
        this.toggleEnabled = toggleEnabled;
    }

    public com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener getOnCheckedChangeListener() {
        return this.onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public int getType() {
        return R.id.material_drawer_item_primary_toggle;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_toggle;
    }

    public void bindView(final ViewHolder viewHolder) {
        bindViewHelper(viewHolder);
        viewHolder.toggle.setOnCheckedChangeListener(null);
        viewHolder.toggle.setChecked(this.checked);
        viewHolder.toggle.setOnCheckedChangeListener(this.checkedChangeListener);
        viewHolder.toggle.setEnabled(this.toggleEnabled);
        withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (!ToggleDrawerItem.this.isSelectable()) {
                    boolean z;
                    ToggleDrawerItem toggleDrawerItem = ToggleDrawerItem.this;
                    if (ToggleDrawerItem.this.checked) {
                        z = false;
                    } else {
                        z = true;
                    }
                    toggleDrawerItem.checked = z;
                    viewHolder.toggle.setChecked(ToggleDrawerItem.this.checked);
                }
                return false;
            }
        });
        onPostBindView(this, viewHolder.itemView);
    }

    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }
}
