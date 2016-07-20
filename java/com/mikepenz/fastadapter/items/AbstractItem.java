package com.mikepenz.fastadapter.items;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mikepenz.fastadapter.FastAdapter.OnClickListener;
import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractItem<Item extends AbstractItem<?, ?>, VH extends ViewHolder> implements IItem<Item, VH>, IClickable<Item> {
    protected boolean mEnabled = true;
    protected ViewHolderFactory<? extends VH> mFactory;
    protected long mIdentifier = -1;
    protected OnClickListener<Item> mOnItemClickListener;
    protected OnClickListener<Item> mOnItemPreClickListener;
    protected boolean mSelectable = true;
    protected boolean mSelected = false;
    protected Object mTag;

    protected static class ReflectionBasedViewHolderFactory<VH extends ViewHolder> implements ViewHolderFactory<VH> {
        private final Class<? extends VH> clazz;

        public ReflectionBasedViewHolderFactory(Class<? extends VH> clazz) {
            this.clazz = clazz;
        }

        public VH create(View v) {
            try {
                Constructor<? extends VH> constructor = this.clazz.getDeclaredConstructor(new Class[]{View.class});
                constructor.setAccessible(true);
                return (ViewHolder) constructor.newInstance(new Object[]{v});
            } catch (NoSuchMethodException e) {
                try {
                    return (ViewHolder) this.clazz.newInstance();
                } catch (Exception e2) {
                    throw new RuntimeException("You have to provide a ViewHolder with a constructor which takes a view!");
                }
            }
        }
    }

    public Item withIdentifier(long identifier) {
        this.mIdentifier = identifier;
        return this;
    }

    public long getIdentifier() {
        return this.mIdentifier;
    }

    public Item withTag(Object object) {
        this.mTag = object;
        return this;
    }

    public Object getTag() {
        return this.mTag;
    }

    public Item withEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public Item withSetSelected(boolean selected) {
        this.mSelected = selected;
        return this;
    }

    public boolean isSelected() {
        return this.mSelected;
    }

    public Item withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return this;
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public Item withOnItemPreClickListener(OnClickListener<Item> onItemPreClickListener) {
        this.mOnItemPreClickListener = onItemPreClickListener;
        return this;
    }

    public OnClickListener<Item> getOnPreItemClickListener() {
        return this.mOnItemPreClickListener;
    }

    public Item withOnItemClickListener(OnClickListener<Item> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public OnClickListener<Item> getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    @CallSuper
    public void bindView(VH holder) {
        holder.itemView.setSelected(isSelected());
        holder.itemView.setTag(this);
    }

    public View generateView(Context ctx) {
        VH viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    public View generateView(Context ctx, ViewGroup parent) {
        VH viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    public VH getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    public Item withFactory(ViewHolderFactory<? extends VH> factory) {
        this.mFactory = factory;
        return this;
    }

    public ViewHolderFactory<? extends VH> getFactory() {
        if (this.mFactory == null) {
            try {
                this.mFactory = new ReflectionBasedViewHolderFactory(viewHolderType());
            } catch (Exception e) {
                throw new RuntimeException("please set a ViewHolderFactory");
            }
        }
        return this.mFactory;
    }

    protected Class<? extends VH> viewHolderType() {
        return (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public VH getViewHolder(View v) {
        return getFactory().create(v);
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
        if (this.mIdentifier != ((AbstractItem) o).mIdentifier) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Long.valueOf(this.mIdentifier).hashCode();
    }
}
