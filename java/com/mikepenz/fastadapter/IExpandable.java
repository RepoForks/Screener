package com.mikepenz.fastadapter;

import java.util.List;

public interface IExpandable<T, Item extends IItem> {
    List<Item> getSubItems();

    boolean isAutoExpanding();

    boolean isExpanded();

    T withIsExpanded(boolean z);

    T withSubItems(List<Item> list);
}
