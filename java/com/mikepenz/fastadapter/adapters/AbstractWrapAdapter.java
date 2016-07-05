package com.mikepenz.fastadapter.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.mikepenz.fastadapter.IItem;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWrapAdapter<Item extends IItem> extends Adapter {
    private Adapter mAdapter;
    private List<Item> mItems = new ArrayList();

    public abstract int itemInsertedBeforeCount(int i);

    public abstract boolean shouldInsertItemAtPosition(int i);

    public AbstractWrapAdapter(List<Item> items) {
        this.mItems = items;
    }

    public List<Item> getItems() {
        return this.mItems;
    }

    public void setItems(List<Item> items) {
        this.mItems = items;
    }

    public AbstractWrapAdapter wrap(Adapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public void registerAdapterDataObserver(AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (this.mAdapter != null) {
            this.mAdapter.registerAdapterDataObserver(observer);
        }
    }

    public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    public int getItemViewType(int position) {
        if (shouldInsertItemAtPosition(position)) {
            return getItem(position).getType();
        }
        return this.mAdapter.getItemViewType(position - itemInsertedBeforeCount(position));
    }

    public long getItemId(int position) {
        if (shouldInsertItemAtPosition(position)) {
            return getItem(position).getIdentifier();
        }
        return this.mAdapter.getItemId(position - itemInsertedBeforeCount(position));
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public Item getItem(int position) {
        if (shouldInsertItemAtPosition(position)) {
            return (IItem) this.mItems.get(itemInsertedBeforeCount(position - 1));
        }
        return null;
    }

    public int getItemCount() {
        int itemCount = this.mAdapter.getItemCount();
        return itemInsertedBeforeCount(itemCount) + itemCount;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (IItem item : this.mItems) {
            if (item.getType() == viewType) {
                return item.getViewHolder(parent);
            }
        }
        return this.mAdapter.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (shouldInsertItemAtPosition(position)) {
            getItem(position).bindView(holder);
        } else {
            this.mAdapter.onBindViewHolder(holder, position - itemInsertedBeforeCount(position));
        }
    }

    public void setHasStableIds(boolean hasStableIds) {
        this.mAdapter.setHasStableIds(hasStableIds);
    }

    public void onViewRecycled(ViewHolder holder) {
        this.mAdapter.onViewRecycled(holder);
    }

    public boolean onFailedToRecycleView(ViewHolder holder) {
        return this.mAdapter.onFailedToRecycleView(holder);
    }

    public void onViewDetachedFromWindow(ViewHolder holder) {
        this.mAdapter.onViewDetachedFromWindow(holder);
    }

    public void onViewAttachedToWindow(ViewHolder holder) {
        this.mAdapter.onViewAttachedToWindow(holder);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mAdapter.onDetachedFromRecyclerView(recyclerView);
    }
}
