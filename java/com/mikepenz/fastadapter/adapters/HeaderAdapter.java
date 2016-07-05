package com.mikepenz.fastadapter.adapters;

import com.mikepenz.fastadapter.IItem;

public class HeaderAdapter<Item extends IItem> extends ItemAdapter<Item> {
    public int getOrder() {
        return 100;
    }
}
