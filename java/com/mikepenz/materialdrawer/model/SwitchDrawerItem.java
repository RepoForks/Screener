package com.mikepenz.materialdrawer.model;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class SwitchDrawerItem extends BasePrimaryDrawerItem<SwitchDrawerItem, ViewHolder> {
    private boolean checked = false;
    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (SwitchDrawerItem.this.isEnabled()) {
                SwitchDrawerItem.this.checked = isChecked;
                if (SwitchDrawerItem.this.getOnCheckedChangeListener() != null) {
                    SwitchDrawerItem.this.getOnCheckedChangeListener().onCheckedChanged(SwitchDrawerItem.this, buttonView, isChecked);
                    return;
                }
                return;
            }
            buttonView.setOnCheckedChangeListener(null);
            buttonView.setChecked(!isChecked);
            buttonView.setOnCheckedChangeListener(SwitchDrawerItem.this.checkedChangeListener);
        }
    };
    private com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener onCheckedChangeListener = null;
    private boolean switchEnabled = true;

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        private SwitchCompat switchView;

        private ViewHolder(View view) {
            super(view);
            this.switchView = (SwitchCompat) view.findViewById(R.id.material_drawer_switch);
        }
    }

    public SwitchDrawerItem withChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public SwitchDrawerItem withSwitchEnabled(boolean switchEnabled) {
        this.switchEnabled = switchEnabled;
        return this;
    }

    public SwitchDrawerItem withOnCheckedChangeListener(com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public SwitchDrawerItem withCheckable(boolean checkable) {
        return (SwitchDrawerItem) withSelectable(checkable);
    }

    public boolean isChecked() {
        return this.checked;
    }

    public boolean isSwitchEnabled() {
        return this.switchEnabled;
    }

    public com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener getOnCheckedChangeListener() {
        return this.onCheckedChangeListener;
    }

    public int getType() {
        return R.id.material_drawer_item_primary_switch;
    }

    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_switch;
    }

    public void bindView(final ViewHolder viewHolder) {
        bindViewHelper(viewHolder);
        viewHolder.switchView.setOnCheckedChangeListener(null);
        viewHolder.switchView.setChecked(this.checked);
        viewHolder.switchView.setOnCheckedChangeListener(this.checkedChangeListener);
        viewHolder.switchView.setEnabled(this.switchEnabled);
        withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (!SwitchDrawerItem.this.isSelectable()) {
                    boolean z;
                    SwitchDrawerItem switchDrawerItem = SwitchDrawerItem.this;
                    if (SwitchDrawerItem.this.checked) {
                        z = false;
                    } else {
                        z = true;
                    }
                    switchDrawerItem.checked = z;
                    viewHolder.switchView.setChecked(SwitchDrawerItem.this.checked);
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
