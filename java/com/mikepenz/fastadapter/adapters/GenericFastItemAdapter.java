package com.mikepenz.fastadapter.adapters;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.Function;
import java.util.List;

public class GenericFastItemAdapter<Model, Item extends GenericAbstractItem<Model, Item, ?>> extends FastAdapter<Item> {
    private final GenericItemAdapter<Model, Item> mItemAdapter;

    public GenericFastItemAdapter(Class<? extends Item> itemClass, Class<? extends Model> modelClass) {
        this.mItemAdapter = new GenericItemAdapter(itemClass, modelClass);
        this.mItemAdapter.wrap((FastAdapter) this);
    }

    public GenericFastItemAdapter(Function<Model, Item> itemFactory) {
        this.mItemAdapter = new GenericItemAdapter(itemFactory);
        this.mItemAdapter.wrap((FastAdapter) this);
    }

    public GenericItemAdapter<Model, Item> getGenericItemAdapter() {
        return this.mItemAdapter;
    }

    public List<Model> getModels() {
        return this.mItemAdapter.getModels();
    }

    public GenericFastItemAdapter<Model, Item> setModel(List<Model> models) {
        this.mItemAdapter.setModel(models);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> setNewModel(List<Model> models) {
        this.mItemAdapter.setNewModel(models);
        return this;
    }

    @SafeVarargs
    public final GenericFastItemAdapter<Model, Item> addModel(Model... models) {
        this.mItemAdapter.addModel((Object[]) models);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> addModel(List<Model> models) {
        this.mItemAdapter.addModel((List) models);
        return this;
    }

    @SafeVarargs
    public final GenericFastItemAdapter<Model, Item> addModel(int position, Model... models) {
        this.mItemAdapter.addModel(position, (Object[]) models);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> addModel(int position, List<Model> models) {
        this.mItemAdapter.addModel(position, (List) models);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> setModel(int position, Model model) {
        this.mItemAdapter.setModel(position, model);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> clearModel() {
        this.mItemAdapter.clearModel();
        return this;
    }

    public GenericFastItemAdapter<Model, Item> moveModel(int fromPosition, int toPosition) {
        this.mItemAdapter.moveModel(fromPosition, toPosition);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> removeModelRange(int position, int itemCount) {
        this.mItemAdapter.removeModelRange(position, itemCount);
        return this;
    }

    public GenericFastItemAdapter<Model, Item> removeModel(int position) {
        this.mItemAdapter.removeModel(position);
        return this;
    }
}
