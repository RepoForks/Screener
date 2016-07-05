package com.mikepenz.fastadapter;

import java.util.List;

public interface IAdapter<Item extends IItem> {
    Item getAdapterItem(int i);

    int getAdapterItemCount();

    List<Item> getAdapterItems();

    int getAdapterPosition(Item item);

    FastAdapter<Item> getFastAdapter();

    int getGlobalPosition(int i);

    Item getItem(int i);

    int getItemCount();

    int getOrder();
}
