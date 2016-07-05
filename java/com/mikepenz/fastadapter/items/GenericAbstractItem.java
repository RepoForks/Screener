package com.mikepenz.fastadapter.items;

import android.support.v7.widget.RecyclerView.ViewHolder;
import java.lang.reflect.ParameterizedType;

public abstract class GenericAbstractItem<Model, Item extends GenericAbstractItem<?, ?, ?>, VH extends ViewHolder> extends AbstractItem<Item, VH> {
    private Model mModel;

    public GenericAbstractItem(Model model) {
        this.mModel = model;
    }

    public Model getModel() {
        return this.mModel;
    }

    public GenericAbstractItem<?, ?, ?> setModel(Model model) {
        this.mModel = model;
        return this;
    }

    protected Class<? extends VH> viewHolderType() {
        return (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
    }
}
