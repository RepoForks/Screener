package com.mikepenz.materialdrawer.interfaces;

import android.widget.CompoundButton;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public interface OnCheckedChangeListener {
    void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean z);
}
