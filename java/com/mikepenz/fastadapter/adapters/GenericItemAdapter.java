package com.mikepenz.fastadapter.adapters;

import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.Function;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericItemAdapter<Model, Item extends GenericAbstractItem<Model, Item, ?>> extends ItemAdapter<Item> {
    private final Function<Model, Item> mItemFactory;

    protected static class ReflectionBasedItemFactory<Model, Item> implements Function<Model, Item> {
        private final Class<? extends Item> itemClass;
        private final Class<? extends Model> modelClass;

        public ReflectionBasedItemFactory(Class<? extends Model> modelClass, Class<? extends Item> itemClass) {
            this.modelClass = modelClass;
            this.itemClass = itemClass;
        }

        public Item apply(Model model) {
            try {
                Constructor<? extends Item> constructor = this.itemClass.getDeclaredConstructor(new Class[]{this.modelClass});
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[]{model});
            } catch (Exception e) {
                throw new RuntimeException("Please provide a constructor that takes a model as an argument");
            }
        }
    }

    public GenericItemAdapter(Class<? extends Item> itemClass, Class<? extends Model> modelClass) {
        this(new ReflectionBasedItemFactory(modelClass, itemClass));
    }

    public GenericItemAdapter(Function<Model, Item> itemFactory) {
        this.mItemFactory = itemFactory;
    }

    public List<Model> getModels() {
        List<Model> models = new ArrayList();
        for (GenericAbstractItem item : getAdapterItems()) {
            models.add(item.getModel());
        }
        return models;
    }

    public GenericItemAdapter<Model, Item> setModel(List<Model> models) {
        super.set(toItems(models));
        return this;
    }

    public GenericItemAdapter<Model, Item> setNewModel(List<Model> models) {
        super.setNewList(toItems(models));
        return this;
    }

    @SafeVarargs
    public final GenericItemAdapter<Model, Item> addModel(Model... models) {
        addModel(Arrays.asList(models));
        return this;
    }

    public GenericItemAdapter<Model, Item> addModel(List<Model> models) {
        super.add(toItems(models));
        return this;
    }

    @SafeVarargs
    public final GenericItemAdapter<Model, Item> addModel(int position, Model... models) {
        addModel(position, Arrays.asList(models));
        return this;
    }

    public GenericItemAdapter<Model, Item> addModel(int position, List<Model> models) {
        super.add(position, toItems(models));
        return this;
    }

    public GenericItemAdapter<Model, Item> setModel(int position, Model model) {
        super.set(position, toItem(model));
        return this;
    }

    public GenericItemAdapter<Model, Item> clearModel() {
        super.clear();
        return this;
    }

    public GenericItemAdapter<Model, Item> moveModel(int fromPosition, int toPosition) {
        super.move(fromPosition, toPosition);
        return this;
    }

    public GenericItemAdapter<Model, Item> removeModelRange(int position, int itemCount) {
        super.removeRange(position, itemCount);
        return this;
    }

    public GenericItemAdapter<Model, Item> removeModel(int position) {
        super.remove(position);
        return this;
    }

    protected List<Item> toItems(List<Model> models) {
        if (models == null) {
            return Collections.emptyList();
        }
        List<Item> items = new ArrayList(models.size());
        for (Model model : models) {
            items.add(toItem(model));
        }
        return items;
    }

    protected Item toItem(Model model) {
        return (GenericAbstractItem) this.mItemFactory.apply(model);
    }
}
