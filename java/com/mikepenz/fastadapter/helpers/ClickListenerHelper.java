package com.mikepenz.fastadapter.helpers;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;

public class ClickListenerHelper<Item extends IItem> {
    private FastAdapter<Item> mFastAdapter;

    public interface OnClickListener<Item extends IItem> {
        void onClick(View view, int i, Item item);
    }

    public ClickListenerHelper(FastAdapter<Item> fastAdapter) {
        this.mFastAdapter = fastAdapter;
    }

    public void listen(final ViewHolder viewHolder, View view, final OnClickListener<Item> onClickListener) {
        view.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                if (pos != -1) {
                    onClickListener.onClick(v, pos, ClickListenerHelper.this.mFastAdapter.getItem(pos));
                }
            }
        });
    }

    public void listen(final ViewHolder viewHolder, @IdRes int viewId, final OnClickListener<Item> onClickListener) {
        viewHolder.itemView.findViewById(viewId).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                if (pos != -1) {
                    onClickListener.onClick(v, pos, ClickListenerHelper.this.mFastAdapter.getItem(pos));
                }
            }
        });
    }
}
