package com.mikepenz.fastadapter;

import com.mikepenz.fastadapter.FastAdapter.OnClickListener;

public interface IClickable<Item extends IItem> {
    OnClickListener<Item> getOnItemClickListener();

    OnClickListener<Item> getOnPreItemClickListener();

    Item withOnItemClickListener(OnClickListener<Item> onClickListener);

    Item withOnItemPreClickListener(OnClickListener<Item> onClickListener);
}
