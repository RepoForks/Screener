package com.mikepenz.fastadapter.adapters;

import android.widget.Filter;
import android.widget.Filter.FilterResults;
import com.mikepenz.fastadapter.AbstractAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.IItemAdapter.Predicate;
import com.mikepenz.fastadapter.utils.IdDistributor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemAdapter<Item extends IItem> extends AbstractAdapter<Item> implements IItemAdapter<Item> {
    protected Comparator<Item> mComparator;
    private Predicate<Item> mFilterPredicate;
    private Filter mItemFilter = new ItemFilter();
    protected ItemFilterListener mItemFilterListener;
    private List<Item> mItems = new ArrayList();
    private boolean mUseIdDistributor = true;

    public class ItemFilter extends Filter {
        private CharSequence mConstraint;
        private List<Item> mOriginalItems;

        protected FilterResults performFiltering(CharSequence constraint) {
            if (ItemAdapter.this.getFastAdapter().isPositionBasedStateManagement()) {
                ItemAdapter.this.getFastAdapter().deselect();
            }
            ItemAdapter.this.getFastAdapter().collapse(false);
            this.mConstraint = constraint;
            if (this.mOriginalItems == null) {
                this.mOriginalItems = new ArrayList(ItemAdapter.this.mItems);
            }
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = this.mOriginalItems;
                results.count = this.mOriginalItems.size();
                this.mOriginalItems = null;
            } else {
                List<Item> filteredItems = new ArrayList();
                if (ItemAdapter.this.mFilterPredicate != null) {
                    for (IItem item : this.mOriginalItems) {
                        if (!ItemAdapter.this.mFilterPredicate.filter(item, constraint)) {
                            filteredItems.add(item);
                        }
                    }
                } else {
                    filteredItems = ItemAdapter.this.mItems;
                }
                results.values = filteredItems;
                results.count = filteredItems.size();
            }
            return results;
        }

        public CharSequence getConstraint() {
            return this.mConstraint;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                ItemAdapter.this.set((List) results.values);
            }
            if (ItemAdapter.this.mItemFilterListener != null) {
                ItemAdapter.this.mItemFilterListener.itemsFiltered();
            }
        }

        public Set<Integer> getSelections() {
            if (this.mOriginalItems == null) {
                return ItemAdapter.this.getFastAdapter().getSelections();
            }
            Set<Integer> hashSet = new HashSet();
            int length = this.mOriginalItems.size();
            int adapterOffset = ItemAdapter.this.getFastAdapter().getPreItemCountByOrder(ItemAdapter.this.getOrder());
            for (int i = 0; i < length; i++) {
                if (((IItem) this.mOriginalItems.get(i)).isSelected()) {
                    hashSet.add(Integer.valueOf(i + adapterOffset));
                }
            }
            return hashSet;
        }

        public Set<Item> getSelectedItems() {
            if (this.mOriginalItems == null) {
                return ItemAdapter.this.getFastAdapter().getSelectedItems();
            }
            Set<Item> hashSet = new HashSet();
            int length = this.mOriginalItems.size();
            for (int i = 0; i < length; i++) {
                IItem item = (IItem) this.mOriginalItems.get(i);
                if (item.isSelected()) {
                    hashSet.add(item);
                }
            }
            return hashSet;
        }
    }

    public interface ItemFilterListener {
        void itemsFiltered();
    }

    public ItemAdapter withUseIdDistributor(boolean useIdDistributor) {
        this.mUseIdDistributor = useIdDistributor;
        return this;
    }

    public ItemAdapter<Item> withItemFilter(Filter itemFilter) {
        this.mItemFilter = itemFilter;
        return this;
    }

    public Filter getItemFilter() {
        return this.mItemFilter;
    }

    public ItemAdapter<Item> withFilterPredicate(Predicate<Item> filterPredicate) {
        this.mFilterPredicate = filterPredicate;
        return this;
    }

    public void filter(CharSequence constraint) {
        this.mItemFilter.filter(constraint);
    }

    public ItemAdapter<Item> withItemFilterListener(ItemFilterListener listener) {
        this.mItemFilterListener = listener;
        return this;
    }

    public ItemAdapter<Item> withComparator(Comparator<Item> comparator) {
        return withComparator(comparator, true);
    }

    public ItemAdapter<Item> withComparator(Comparator<Item> comparator, boolean sortNow) {
        this.mComparator = comparator;
        if (!(this.mItems == null || this.mComparator == null || !sortNow)) {
            Collections.sort(this.mItems, this.mComparator);
            getFastAdapter().notifyAdapterDataSetChanged();
        }
        return this;
    }

    public Comparator<Item> getComparator() {
        return this.mComparator;
    }

    public int getOrder() {
        return 500;
    }

    public int getAdapterItemCount() {
        return this.mItems.size();
    }

    public List<Item> getAdapterItems() {
        return this.mItems;
    }

    public int getAdapterPosition(Item item) {
        int length = this.mItems.size();
        for (int i = 0; i < length; i++) {
            if (((IItem) this.mItems.get(i)).getIdentifier() == item.getIdentifier()) {
                return i;
            }
        }
        return -1;
    }

    public int getGlobalPosition(int position) {
        return getFastAdapter().getPreItemCountByOrder(getOrder()) + position;
    }

    public Item getAdapterItem(int position) {
        return (IItem) this.mItems.get(position);
    }

    public <T> T setSubItems(IExpandable<T, Item> collapsible, List<Item> subItems) {
        if (this.mUseIdDistributor) {
            IdDistributor.checkIds((List) subItems);
        }
        return collapsible.withSubItems(subItems);
    }

    public ItemAdapter<Item> set(List<Item> items) {
        if (this.mUseIdDistributor) {
            IdDistributor.checkIds((List) items);
        }
        getFastAdapter().collapse(false);
        int newItemsCount = items.size();
        int previousItemsCount = this.mItems.size();
        int itemsBeforeThisAdapter = getFastAdapter().getPreItemCountByOrder(getOrder());
        if (items != this.mItems) {
            if (!this.mItems.isEmpty()) {
                this.mItems.clear();
            }
            this.mItems.addAll(items);
        }
        mapPossibleTypes(items);
        if (this.mComparator != null) {
            Collections.sort(this.mItems, this.mComparator);
        }
        if (newItemsCount > previousItemsCount) {
            if (previousItemsCount > 0) {
                getFastAdapter().notifyAdapterItemRangeChanged(itemsBeforeThisAdapter, previousItemsCount);
            }
            getFastAdapter().notifyAdapterItemRangeInserted(itemsBeforeThisAdapter + previousItemsCount, newItemsCount - previousItemsCount);
        } else if (newItemsCount > 0 && newItemsCount < previousItemsCount) {
            getFastAdapter().notifyAdapterItemRangeChanged(itemsBeforeThisAdapter, newItemsCount);
            getFastAdapter().notifyAdapterItemRangeRemoved(itemsBeforeThisAdapter + newItemsCount, previousItemsCount - newItemsCount);
        } else if (newItemsCount == 0) {
            getFastAdapter().notifyAdapterItemRangeRemoved(itemsBeforeThisAdapter, previousItemsCount);
        } else {
            getFastAdapter().notifyAdapterDataSetChanged();
        }
        return this;
    }

    public ItemAdapter<Item> setNewList(List<Item> items) {
        if (this.mUseIdDistributor) {
            IdDistributor.checkIds((List) items);
        }
        this.mItems = new ArrayList(items);
        mapPossibleTypes(this.mItems);
        if (this.mComparator != null) {
            Collections.sort(this.mItems, this.mComparator);
        }
        getFastAdapter().notifyAdapterDataSetChanged();
        return this;
    }

    @SafeVarargs
    public final ItemAdapter<Item> add(Item... items) {
        return add(Arrays.asList(items));
    }

    public ItemAdapter<Item> add(List<Item> items) {
        if (this.mUseIdDistributor) {
            IdDistributor.checkIds((List) items);
        }
        int countBefore = this.mItems.size();
        this.mItems.addAll(items);
        mapPossibleTypes(items);
        if (this.mComparator == null) {
            getFastAdapter().notifyAdapterItemRangeInserted(getFastAdapter().getPreItemCountByOrder(getOrder()) + countBefore, items.size());
        } else {
            Collections.sort(this.mItems, this.mComparator);
            getFastAdapter().notifyAdapterDataSetChanged();
        }
        return this;
    }

    @SafeVarargs
    public final ItemAdapter<Item> add(int position, Item... items) {
        return add(position, Arrays.asList(items));
    }

    public ItemAdapter<Item> add(int position, List<Item> items) {
        if (this.mUseIdDistributor) {
            IdDistributor.checkIds((List) items);
        }
        if (items != null) {
            this.mItems.addAll(position - getFastAdapter().getPreItemCount(position), items);
            mapPossibleTypes(items);
            getFastAdapter().notifyAdapterItemRangeInserted(position, items.size());
        }
        return this;
    }

    public ItemAdapter<Item> set(int position, Item item) {
        if (this.mUseIdDistributor) {
            IdDistributor.checkId(item);
        }
        this.mItems.set(position - getFastAdapter().getPreItemCount(position), item);
        mapPossibleType(item);
        getFastAdapter().notifyAdapterItemChanged(position);
        return this;
    }

    public ItemAdapter<Item> move(int fromPosition, int toPosition) {
        int preItemCount = getFastAdapter().getPreItemCount(fromPosition);
        IItem item = (IItem) this.mItems.get(fromPosition - preItemCount);
        this.mItems.remove(fromPosition - preItemCount);
        this.mItems.add(toPosition - preItemCount, item);
        getFastAdapter().notifyAdapterItemMoved(fromPosition, toPosition);
        return this;
    }

    public ItemAdapter<Item> remove(int position) {
        this.mItems.remove(position - getFastAdapter().getPreItemCount(position));
        getFastAdapter().notifyAdapterItemRemoved(position);
        return this;
    }

    public ItemAdapter<Item> removeRange(int position, int itemCount) {
        int length = this.mItems.size();
        int preItemCount = getFastAdapter().getPreItemCount(position);
        int saveItemCount = Math.min(itemCount, (length - position) + preItemCount);
        for (int i = 0; i < saveItemCount; i++) {
            this.mItems.remove(position - preItemCount);
        }
        getFastAdapter().notifyAdapterItemRangeRemoved(position, saveItemCount);
        return this;
    }

    public ItemAdapter<Item> clear() {
        int count = this.mItems.size();
        this.mItems.clear();
        getFastAdapter().notifyAdapterItemRangeRemoved(getFastAdapter().getPreItemCountByOrder(getOrder()), count);
        return this;
    }
}
