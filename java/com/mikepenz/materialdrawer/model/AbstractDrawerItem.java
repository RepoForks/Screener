package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IIdentifyable;
import com.mikepenz.fastadapter.utils.IdDistributor;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnPostBindViewListener;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDrawerItem<T, VH extends ViewHolder> implements IDrawerItem<T, VH>, Selectable<T>, Tagable<T>, IExpandable<T, IDrawerItem> {
    protected boolean mEnabled = true;
    private boolean mExpanded = false;
    protected long mIdentifier = -1;
    public OnDrawerItemClickListener mOnDrawerItemClickListener = null;
    protected OnPostBindViewListener mOnPostBindViewListener = null;
    protected boolean mSelectable = true;
    protected boolean mSelected = false;
    protected List<IDrawerItem> mSubItems;
    protected Object mTag;

    public abstract ViewHolderFactory<VH> getFactory();

    public T withIdentifier(long identifier) {
        this.mIdentifier = identifier;
        return this;
    }

    public long getIdentifier() {
        return this.mIdentifier;
    }

    public T withTag(Object object) {
        this.mTag = object;
        return this;
    }

    public Object getTag() {
        return this.mTag;
    }

    public T withEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public T withSetSelected(boolean selected) {
        this.mSelected = selected;
        return this;
    }

    public boolean isSelected() {
        return this.mSelected;
    }

    public T withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return this;
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return this.mOnDrawerItemClickListener;
    }

    public T withOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    public OnPostBindViewListener getOnPostBindViewListener() {
        return this.mOnPostBindViewListener;
    }

    public T withPostOnBindViewListener(OnPostBindViewListener onPostBindViewListener) {
        this.mOnPostBindViewListener = onPostBindViewListener;
        return this;
    }

    public void onPostBindView(IDrawerItem drawerItem, View view) {
        if (this.mOnPostBindViewListener != null) {
            this.mOnPostBindViewListener.onBindView(drawerItem, view);
        }
    }

    public T withSubItems(List<IDrawerItem> subItems) {
        this.mSubItems = IdDistributor.checkIds((List) subItems);
        return this;
    }

    public T withSubItems(IDrawerItem... subItems) {
        if (this.mSubItems == null) {
            this.mSubItems = new ArrayList();
        }
        Collections.addAll(this.mSubItems, IdDistributor.checkIds((IIdentifyable[]) subItems));
        return this;
    }

    public List<IDrawerItem> getSubItems() {
        return this.mSubItems;
    }

    public T withIsExpanded(boolean expanded) {
        this.mExpanded = expanded;
        return this;
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public boolean isAutoExpanding() {
        return true;
    }

    public View generateView(Context ctx) {
        VH viewHolder = getFactory().create(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    public View generateView(Context ctx, ViewGroup parent) {
        VH viewHolder = getFactory().create(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    public VH getViewHolder(ViewGroup parent) {
        return getFactory().create(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    public boolean equals(long id) {
        return id == this.mIdentifier;
    }

    public boolean equals(int id) {
        return ((long) id) == this.mIdentifier;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.mIdentifier != ((AbstractDrawerItem) o).mIdentifier) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Long.valueOf(this.mIdentifier).hashCode();
    }
}
