package com.mikepenz.fastadapter;

import java.util.List;

public interface IItemAdapter<Item extends IItem> extends IAdapter<Item> {

    public interface Predicate<Item extends IItem> {
        boolean filter(Item item, CharSequence charSequence);
    }

    IItemAdapter<Item> add(int i, List<Item> list);

    IItemAdapter<Item> add(int i, Item... itemArr);

    IItemAdapter<Item> add(List<Item> list);

    IItemAdapter<Item> add(Item... itemArr);

    IItemAdapter<Item> clear();

    IItemAdapter<Item> remove(int i);

    IItemAdapter<Item> removeRange(int i, int i2);

    IItemAdapter<Item> set(int i, Item item);

    IItemAdapter<Item> set(List<Item> list);

    IItemAdapter<Item> setNewList(List<Item> list);

    <T> T setSubItems(IExpandable<T, Item> iExpandable, List<Item> list);
}
