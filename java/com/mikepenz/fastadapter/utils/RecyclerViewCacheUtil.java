package com.mikepenz.fastadapter.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.mikepenz.fastadapter.IItem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

public class RecyclerViewCacheUtil<Item extends IItem> {
    private int mCacheSize = 2;

    public RecyclerViewCacheUtil withCacheSize(int cacheSize) {
        this.mCacheSize = cacheSize;
        return this;
    }

    public void apply(RecyclerView recyclerView, Iterable<Item> items) {
        if (items != null) {
            HashMap<Integer, Stack<ViewHolder>> cache = new HashMap();
            for (Item d : items) {
                if (!cache.containsKey(Integer.valueOf(d.getType()))) {
                    cache.put(Integer.valueOf(d.getType()), new Stack());
                }
                if (this.mCacheSize == -1 || ((Stack) cache.get(Integer.valueOf(d.getType()))).size() <= this.mCacheSize) {
                    ((Stack) cache.get(Integer.valueOf(d.getType()))).push(d.getViewHolder(recyclerView));
                }
                RecycledViewPool recyclerViewPool = new RecycledViewPool();
                for (Entry<Integer, Stack<ViewHolder>> entry : cache.entrySet()) {
                    recyclerViewPool.setMaxRecycledViews(((Integer) entry.getKey()).intValue(), this.mCacheSize);
                    Iterator it = ((Stack) entry.getValue()).iterator();
                    while (it.hasNext()) {
                        recyclerViewPool.putRecycledView((ViewHolder) it.next());
                    }
                    ((Stack) entry.getValue()).clear();
                }
                cache.clear();
                recyclerView.setRecycledViewPool(recyclerViewPool);
            }
        }
    }
}
