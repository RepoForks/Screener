package com.mikepenz.fastadapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.util.List;

public abstract class AbstractAdapter<Item extends IItem> extends Adapter implements IAdapter<Item> {
    private FastAdapter<Item> mFastAdapter;

    public AbstractAdapter wrap(FastAdapter fastAdapter) {
        this.mFastAdapter = fastAdapter;
        this.mFastAdapter.registerAdapter(this);
        return this;
    }

    public AbstractAdapter wrap(IAdapter abstractAdapter) {
        this.mFastAdapter = abstractAdapter.getFastAdapter();
        this.mFastAdapter.registerAdapter(this);
        return this;
    }

    public void registerAdapterDataObserver(AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (this.mFastAdapter != null) {
            this.mFastAdapter.registerAdapterDataObserver(observer);
        }
    }

    public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (this.mFastAdapter != null) {
            this.mFastAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    public int getItemViewType(int position) {
        return this.mFastAdapter.getItemViewType(position);
    }

    public long getItemId(int position) {
        return this.mFastAdapter.getItemId(position);
    }

    public FastAdapter<Item> getFastAdapter() {
        return this.mFastAdapter;
    }

    public Item getItem(int position) {
        return this.mFastAdapter.getItem(position);
    }

    public int getItemCount() {
        return this.mFastAdapter.getItemCount();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.mFastAdapter.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        this.mFastAdapter.onBindViewHolder(holder, position);
    }

    public void onBindViewHolder(ViewHolder holder, int position, List payloads) {
        this.mFastAdapter.onBindViewHolder(holder, position, payloads);
    }

    public void setHasStableIds(boolean hasStableIds) {
        this.mFastAdapter.setHasStableIds(hasStableIds);
    }

    public void onViewRecycled(ViewHolder holder) {
        this.mFastAdapter.onViewRecycled(holder);
    }

    public boolean onFailedToRecycleView(ViewHolder holder) {
        return this.mFastAdapter.onFailedToRecycleView(holder);
    }

    public void onViewDetachedFromWindow(ViewHolder holder) {
        this.mFastAdapter.onViewDetachedFromWindow(holder);
    }

    public void onViewAttachedToWindow(ViewHolder holder) {
        this.mFastAdapter.onViewAttachedToWindow(holder);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mFastAdapter.onAttachedToRecyclerView(recyclerView);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mFastAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    public void mapPossibleTypes(Iterable<Item> items) {
        if (items != null) {
            for (Item item : items) {
                mapPossibleType(item);
            }
        }
    }

    public void mapPossibleType(Item item) {
        this.mFastAdapter.registerTypeInstance(item);
    }
}
