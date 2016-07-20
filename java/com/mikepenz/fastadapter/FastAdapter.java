package com.mikepenz.fastadapter;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.mikepenz.fastadapter.utils.AdapterUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class FastAdapter<Item extends IItem> extends Adapter<ViewHolder> {
    protected static final String BUNDLE_EXPANDED = "bundle_expanded";
    protected static final String BUNDLE_SELECTIONS = "bundle_selections";
    private final NavigableMap<Integer, IAdapter<Item>> mAdapterSizes = new TreeMap();
    private final ArrayMap<Integer, IAdapter<Item>> mAdapters = new ArrayMap();
    private boolean mAllowDeselection = true;
    private SparseIntArray mExpanded = new SparseIntArray();
    private int mGlobalSize = 0;
    private boolean mMultiSelect = false;
    private OnBindViewHolderListener mOnBindViewHolderListener = new OnBindViewHolderListenerImpl();
    private OnClickListener<Item> mOnClickListener;
    private OnCreateViewHolderListener mOnCreateViewHolderListener = new OnCreateViewHolderListenerImpl();
    private OnLongClickListener<Item> mOnLongClickListener;
    private OnClickListener<Item> mOnPreClickListener;
    private OnLongClickListener<Item> mOnPreLongClickListener;
    private OnTouchListener<Item> mOnTouchListener;
    private boolean mOnlyOneExpandedItem = false;
    private boolean mPositionBasedStateManagement = true;
    private boolean mSelectOnLongClick = false;
    private boolean mSelectWithItemUpdate = false;
    private boolean mSelectable = false;
    private SortedSet<Integer> mSelections = new TreeSet();
    private final ArrayMap<Integer, Item> mTypeInstances = new ArrayMap();

    public interface OnBindViewHolderListener {
        void onBindViewHolder(ViewHolder viewHolder, int i);
    }

    public class OnBindViewHolderListenerImpl implements OnBindViewHolderListener {
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            FastAdapter.this.getItem(position).bindView(viewHolder);
        }
    }

    public interface OnClickListener<Item extends IItem> {
        boolean onClick(View view, IAdapter<Item> iAdapter, Item item, int i);
    }

    public interface OnCreateViewHolderListener {
        ViewHolder onPostCreateViewHolder(ViewHolder viewHolder);

        ViewHolder onPreCreateViewHolder(ViewGroup viewGroup, int i);
    }

    public class OnCreateViewHolderListenerImpl implements OnCreateViewHolderListener {
        public ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
            return FastAdapter.this.getTypeInstance(viewType).getViewHolder(parent);
        }

        public ViewHolder onPostCreateViewHolder(ViewHolder viewHolder) {
            return viewHolder;
        }
    }

    public interface OnLongClickListener<Item extends IItem> {
        boolean onLongClick(View view, IAdapter<Item> iAdapter, Item item, int i);
    }

    public interface OnTouchListener<Item extends IItem> {
        boolean onTouch(View view, MotionEvent motionEvent, IAdapter<Item> iAdapter, Item item, int i);
    }

    public static class RelativeInfo<Item extends IItem> {
        public IAdapter<Item> adapter = null;
        public Item item = null;
        public int position = -1;
    }

    public FastAdapter() {
        setHasStableIds(true);
    }

    public FastAdapter<Item> withOnClickListener(OnClickListener<Item> onClickListener) {
        this.mOnClickListener = onClickListener;
        return this;
    }

    public FastAdapter<Item> withOnPreClickListener(OnClickListener<Item> onPreClickListener) {
        this.mOnPreClickListener = onPreClickListener;
        return this;
    }

    public FastAdapter<Item> withOnLongClickListener(OnLongClickListener<Item> onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
        return this;
    }

    public FastAdapter<Item> withOnPreLongClickListener(OnLongClickListener<Item> onPreLongClickListener) {
        this.mOnPreLongClickListener = onPreLongClickListener;
        return this;
    }

    public FastAdapter<Item> withOnTouchListener(OnTouchListener<Item> onTouchListener) {
        this.mOnTouchListener = onTouchListener;
        return this;
    }

    public FastAdapter<Item> withOnCreateViewHolderListener(OnCreateViewHolderListener onCreateViewHolderListener) {
        this.mOnCreateViewHolderListener = onCreateViewHolderListener;
        return this;
    }

    public FastAdapter<Item> withOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        this.mOnBindViewHolderListener = onBindViewHolderListener;
        return this;
    }

    public FastAdapter<Item> withSelectWithItemUpdate(boolean selectWithItemUpdate) {
        this.mSelectWithItemUpdate = selectWithItemUpdate;
        return this;
    }

    public FastAdapter<Item> withMultiSelect(boolean multiSelect) {
        this.mMultiSelect = multiSelect;
        return this;
    }

    public FastAdapter<Item> withSelectOnLongClick(boolean selectOnLongClick) {
        this.mSelectOnLongClick = selectOnLongClick;
        return this;
    }

    public FastAdapter<Item> withAllowDeselection(boolean allowDeselection) {
        this.mAllowDeselection = allowDeselection;
        return this;
    }

    public FastAdapter<Item> withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return this;
    }

    public FastAdapter<Item> withPositionBasedStateManagement(boolean mPositionBasedStateManagement) {
        this.mPositionBasedStateManagement = mPositionBasedStateManagement;
        return this;
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public boolean isPositionBasedStateManagement() {
        return this.mPositionBasedStateManagement;
    }

    public FastAdapter<Item> withOnlyOneExpandedItem(boolean mOnlyOneExpandedItem) {
        this.mOnlyOneExpandedItem = mOnlyOneExpandedItem;
        return this;
    }

    public boolean isOnlyOneExpandedItem() {
        return this.mOnlyOneExpandedItem;
    }

    public FastAdapter<Item> withSavedInstanceState(Bundle savedInstanceState) {
        return withSavedInstanceState(savedInstanceState, BuildConfig.FLAVOR);
    }

    public FastAdapter<Item> withSavedInstanceState(Bundle savedInstanceState, String prefix) {
        int i = 0;
        if (savedInstanceState != null) {
            deselect();
            if (this.mPositionBasedStateManagement) {
                int length;
                int[] expandedItems = savedInstanceState.getIntArray(BUNDLE_EXPANDED + prefix);
                if (expandedItems != null) {
                    for (int valueOf : expandedItems) {
                        expand(Integer.valueOf(valueOf).intValue());
                    }
                }
                int[] selections = savedInstanceState.getIntArray(BUNDLE_SELECTIONS + prefix);
                if (selections != null) {
                    length = selections.length;
                    while (i < length) {
                        select(Integer.valueOf(selections[i]).intValue());
                        i++;
                    }
                }
            } else {
                ArrayList<String> expandedItems2 = savedInstanceState.getStringArrayList(BUNDLE_EXPANDED + prefix);
                ArrayList<String> selectedItems = savedInstanceState.getStringArrayList(BUNDLE_SELECTIONS + prefix);
                for (int i2 = 0; i2 < getItemCount(); i2++) {
                    Item item = getItem(i2);
                    String id = String.valueOf(item.getIdentifier());
                    if (expandedItems2 != null && expandedItems2.contains(id)) {
                        expand(i2);
                    }
                    if (selectedItems != null && selectedItems.contains(id)) {
                        select(i2);
                    }
                    AdapterUtil.restoreSubItemSelectionStatesForAlternativeStateManagement(item, selectedItems);
                }
            }
        }
        return this;
    }

    public <A extends AbstractAdapter<Item>> void registerAdapter(A adapter) {
        if (!this.mAdapters.containsKey(Integer.valueOf(adapter.getOrder()))) {
            this.mAdapters.put(Integer.valueOf(adapter.getOrder()), adapter);
            cacheSizes();
        }
    }

    public void registerTypeInstance(Item item) {
        if (!this.mTypeInstances.containsKey(Integer.valueOf(item.getType()))) {
            this.mTypeInstances.put(Integer.valueOf(item.getType()), item);
        }
    }

    public Item getTypeInstance(int type) {
        return (IItem) this.mTypeInstances.get(Integer.valueOf(type));
    }

    public int getHolderAdapterPosition(ViewHolder holder) {
        return holder.getAdapterPosition();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder holder = this.mOnCreateViewHolderListener.onPreCreateViewHolder(parent, viewType);
        holder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                int pos = FastAdapter.this.getHolderAdapterPosition(holder);
                if (pos != -1) {
                    boolean consumed = false;
                    RelativeInfo<Item> relativeInfo = FastAdapter.this.getRelativeInfo(pos);
                    Item item = relativeInfo.item;
                    if (item != null && item.isEnabled()) {
                        if ((item instanceof IClickable) && ((IClickable) item).getOnPreItemClickListener() != null) {
                            consumed = ((IClickable) item).getOnPreItemClickListener().onClick(v, relativeInfo.adapter, item, pos);
                        }
                        if (!(consumed || FastAdapter.this.mOnPreClickListener == null)) {
                            consumed = FastAdapter.this.mOnPreClickListener.onClick(v, relativeInfo.adapter, item, pos);
                        }
                        if (!consumed && (item instanceof IExpandable) && ((IExpandable) item).isAutoExpanding() && ((IExpandable) item).getSubItems() != null) {
                            FastAdapter.this.toggleExpandable(pos);
                        }
                        if (FastAdapter.this.mOnlyOneExpandedItem) {
                            int[] expandedItems = FastAdapter.this.getExpandedItems();
                            for (int i = expandedItems.length - 1; i >= 0; i--) {
                                if (expandedItems[i] != pos) {
                                    FastAdapter.this.collapse(expandedItems[i], true);
                                }
                            }
                        }
                        if (!(consumed || FastAdapter.this.mSelectOnLongClick || !FastAdapter.this.mSelectable)) {
                            FastAdapter.this.handleSelection(v, item, pos);
                        }
                        if ((item instanceof IClickable) && ((IClickable) item).getOnItemClickListener() != null) {
                            consumed = ((IClickable) item).getOnItemClickListener().onClick(v, relativeInfo.adapter, item, pos);
                        }
                        if (!consumed && FastAdapter.this.mOnClickListener != null) {
                            FastAdapter.this.mOnClickListener.onClick(v, relativeInfo.adapter, item, pos);
                        }
                    }
                }
            }
        });
        holder.itemView.setOnLongClickListener(new android.view.View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                int pos = FastAdapter.this.getHolderAdapterPosition(holder);
                if (pos == -1) {
                    return false;
                }
                boolean consumed = false;
                RelativeInfo<Item> relativeInfo = FastAdapter.this.getRelativeInfo(pos);
                if (relativeInfo.item == null || !relativeInfo.item.isEnabled()) {
                    return false;
                }
                if (FastAdapter.this.mOnPreLongClickListener != null) {
                    consumed = FastAdapter.this.mOnPreLongClickListener.onLongClick(v, relativeInfo.adapter, relativeInfo.item, pos);
                }
                if (!consumed && FastAdapter.this.mSelectOnLongClick && FastAdapter.this.mSelectable) {
                    FastAdapter.this.handleSelection(v, relativeInfo.item, pos);
                }
                if (FastAdapter.this.mOnLongClickListener != null) {
                    return FastAdapter.this.mOnLongClickListener.onLongClick(v, relativeInfo.adapter, relativeInfo.item, pos);
                }
                return consumed;
            }
        });
        holder.itemView.setOnTouchListener(new android.view.View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (FastAdapter.this.mOnTouchListener != null) {
                    int pos = FastAdapter.this.getHolderAdapterPosition(holder);
                    if (pos != -1) {
                        RelativeInfo<Item> relativeInfo = FastAdapter.this.getRelativeInfo(pos);
                        return FastAdapter.this.mOnTouchListener.onTouch(v, event, relativeInfo.adapter, relativeInfo.item, pos);
                    }
                }
                return false;
            }
        });
        return this.mOnCreateViewHolderListener.onPostCreateViewHolder(holder);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        this.mOnBindViewHolderListener.onBindViewHolder(holder, position);
    }

    public int getPosition(Item item) {
        if (item.getIdentifier() == -1) {
            Log.e("FastAdapter", "You have to define an identifier for your item to retrieve the position via this method");
            return -1;
        }
        int position = 0;
        int length = this.mAdapters.size();
        for (int i = 0; i < length; i++) {
            IAdapter<Item> adapter = (IAdapter) this.mAdapters.valueAt(i);
            if (adapter.getOrder() >= 0) {
                int relativePosition = adapter.getAdapterPosition(item);
                if (relativePosition != -1) {
                    return position + relativePosition;
                }
                position = adapter.getAdapterItemCount();
            }
        }
        return -1;
    }

    public Item getItem(int position) {
        if (position < 0 || position >= this.mGlobalSize) {
            return null;
        }
        Entry<Integer, IAdapter<Item>> entry = this.mAdapterSizes.floorEntry(Integer.valueOf(position));
        return ((IAdapter) entry.getValue()).getAdapterItem(position - ((Integer) entry.getKey()).intValue());
    }

    public RelativeInfo<Item> getRelativeInfo(int position) {
        if (position < 0) {
            return new RelativeInfo();
        }
        RelativeInfo<Item> relativeInfo = new RelativeInfo();
        Entry<Integer, IAdapter<Item>> entry = this.mAdapterSizes.floorEntry(Integer.valueOf(position));
        if (entry == null) {
            return relativeInfo;
        }
        relativeInfo.item = ((IAdapter) entry.getValue()).getAdapterItem(position - ((Integer) entry.getKey()).intValue());
        relativeInfo.adapter = (IAdapter) entry.getValue();
        relativeInfo.position = position;
        return relativeInfo;
    }

    public IAdapter<Item> getAdapter(int position) {
        if (position < 0 || position >= this.mGlobalSize) {
            return null;
        }
        return (IAdapter) this.mAdapterSizes.floorEntry(Integer.valueOf(position)).getValue();
    }

    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    public long getItemId(int position) {
        return getItem(position).getIdentifier();
    }

    public int getItemCount() {
        return this.mGlobalSize;
    }

    public int getPreItemCountByOrder(int order) {
        if (this.mGlobalSize == 0) {
            return 0;
        }
        int size = 0;
        for (IAdapter<Item> adapter : this.mAdapters.values()) {
            if (adapter.getOrder() == order) {
                return size;
            }
            size += adapter.getAdapterItemCount();
        }
        return size;
    }

    public int getPreItemCount(int position) {
        if (this.mGlobalSize == 0) {
            return 0;
        }
        return ((Integer) this.mAdapterSizes.floorKey(Integer.valueOf(position))).intValue();
    }

    public int getExpandedItemsCount(int from, int position) {
        int totalAddedItems = 0;
        int i;
        if (this.mPositionBasedStateManagement) {
            int length = this.mExpanded.size();
            i = 0;
            while (i < length) {
                if (this.mExpanded.keyAt(i) >= from && this.mExpanded.keyAt(i) < position) {
                    totalAddedItems += this.mExpanded.get(this.mExpanded.keyAt(i));
                } else if (this.mExpanded.keyAt(i) >= position) {
                    break;
                }
                i++;
            }
        } else {
            for (i = from; i < position; i++) {
                Item tmp = getItem(i);
                if (tmp instanceof IExpandable) {
                    IExpandable tmpExpandable = (IExpandable) tmp;
                    if (tmpExpandable.getSubItems() != null && tmpExpandable.isExpanded()) {
                        totalAddedItems += tmpExpandable.getSubItems().size();
                    }
                }
            }
        }
        return totalAddedItems;
    }

    public Bundle saveInstanceState(Bundle savedInstanceState) {
        return saveInstanceState(savedInstanceState, BuildConfig.FLAVOR);
    }

    public Bundle saveInstanceState(Bundle savedInstanceState, String prefix) {
        if (savedInstanceState != null) {
            if (this.mPositionBasedStateManagement) {
                int[] selections = new int[this.mSelections.size()];
                int index = 0;
                for (Integer selection : this.mSelections) {
                    selections[index] = selection.intValue();
                    index++;
                }
                savedInstanceState.putIntArray(BUNDLE_SELECTIONS + prefix, selections);
                savedInstanceState.putIntArray(BUNDLE_EXPANDED + prefix, getExpandedItems());
            } else {
                ArrayList<String> selections2 = new ArrayList();
                ArrayList<String> expandedItems = new ArrayList();
                int length = getItemCount();
                for (int i = 0; i < length; i++) {
                    Item item = getItem(i);
                    if ((item instanceof IExpandable) && ((IExpandable) item).isExpanded()) {
                        expandedItems.add(String.valueOf(item.getIdentifier()));
                    }
                    if (item.isSelected()) {
                        selections2.add(String.valueOf(item.getIdentifier()));
                    }
                    AdapterUtil.findSubItemSelections(item, selections2);
                }
                savedInstanceState.putStringArrayList(BUNDLE_SELECTIONS + prefix, selections2);
                savedInstanceState.putStringArrayList(BUNDLE_EXPANDED + prefix, expandedItems);
            }
        }
        return savedInstanceState;
    }

    private void cacheSizes() {
        this.mAdapterSizes.clear();
        int size = 0;
        if (this.mAdapters.size() > 0) {
            this.mAdapterSizes.put(Integer.valueOf(0), this.mAdapters.valueAt(0));
        }
        for (IAdapter<Item> adapter : this.mAdapters.values()) {
            if (adapter.getAdapterItemCount() > 0) {
                this.mAdapterSizes.put(Integer.valueOf(size), adapter);
                size += adapter.getAdapterItemCount();
            }
        }
        this.mGlobalSize = size;
    }

    public Set<Integer> getSelections() {
        if (this.mPositionBasedStateManagement) {
            return this.mSelections;
        }
        Set<Integer> selections = new HashSet();
        int length = getItemCount();
        for (int i = 0; i < length; i++) {
            if (getItem(i).isSelected()) {
                selections.add(Integer.valueOf(i));
            }
        }
        return selections;
    }

    public Set<Item> getSelectedItems() {
        Set<Item> items = new HashSet();
        for (Integer position : getSelections()) {
            items.add(getItem(position.intValue()));
        }
        return items;
    }

    public void toggleSelection(int position) {
        if (this.mPositionBasedStateManagement) {
            if (this.mSelections.contains(Integer.valueOf(position))) {
                deselect(position);
            } else {
                select(position);
            }
        } else if (getItem(position).isSelected()) {
            deselect(position);
        } else {
            select(position);
        }
    }

    private void handleSelection(View view, Item item, int position) {
        boolean z = true;
        if (!item.isSelectable()) {
            return;
        }
        if (!item.isSelected() || this.mAllowDeselection) {
            boolean selected;
            if (this.mPositionBasedStateManagement) {
                selected = this.mSelections.contains(Integer.valueOf(position));
            } else {
                selected = item.isSelected();
            }
            if (this.mSelectWithItemUpdate || view == null) {
                if (!this.mMultiSelect) {
                    deselect();
                }
                if (selected) {
                    deselect(position);
                    return;
                } else {
                    select(position);
                    return;
                }
            }
            if (!this.mMultiSelect) {
                if (this.mPositionBasedStateManagement) {
                    Iterator<Integer> entries = this.mSelections.iterator();
                    while (entries.hasNext()) {
                        Integer pos = (Integer) entries.next();
                        if (pos.intValue() != position) {
                            deselect(pos.intValue(), entries);
                        }
                    }
                } else {
                    for (Integer intValue : getSelections()) {
                        int pos2 = intValue.intValue();
                        if (pos2 != position) {
                            deselect(pos2);
                        }
                    }
                }
            }
            item.withSetSelected(!selected);
            if (selected) {
                z = false;
            }
            view.setSelected(z);
            if (!this.mPositionBasedStateManagement) {
                return;
            }
            if (!selected) {
                this.mSelections.add(Integer.valueOf(position));
            } else if (this.mSelections.contains(Integer.valueOf(position))) {
                this.mSelections.remove(Integer.valueOf(position));
            }
        }
    }

    public void select(Iterable<Integer> positions) {
        for (Integer position : positions) {
            select(position.intValue());
        }
    }

    public void select(int position) {
        select(position, false);
    }

    public void select(int position, boolean fireEvent) {
        Item item = getItem(position);
        if (item != null) {
            item.withSetSelected(true);
            if (this.mPositionBasedStateManagement) {
                this.mSelections.add(Integer.valueOf(position));
            }
        }
        notifyItemChanged(position);
        if (this.mOnClickListener != null && fireEvent) {
            this.mOnClickListener.onClick(null, getAdapter(position), item, position);
        }
    }

    public void deselect() {
        if (this.mPositionBasedStateManagement) {
            deselect(this.mSelections);
            return;
        }
        for (IItem item : AdapterUtil.getAllItems(this)) {
            item.withSetSelected(false);
        }
        notifyDataSetChanged();
    }

    public void select() {
        if (this.mPositionBasedStateManagement) {
            int length = getItemCount();
            for (int i = 0; i < length; i++) {
                select(i);
            }
            return;
        }
        for (IItem item : AdapterUtil.getAllItems(this)) {
            item.withSetSelected(true);
        }
        notifyDataSetChanged();
    }

    public void deselect(Iterable<Integer> positions) {
        Iterator<Integer> entries = positions.iterator();
        while (entries.hasNext()) {
            deselect(((Integer) entries.next()).intValue(), entries);
        }
    }

    public void deselect(int position) {
        deselect(position, null);
    }

    private void deselect(int position, Iterator<Integer> entries) {
        Item item = getItem(position);
        if (item != null) {
            item.withSetSelected(false);
        }
        if (entries != null) {
            entries.remove();
        } else if (this.mPositionBasedStateManagement && this.mSelections.contains(Integer.valueOf(position))) {
            this.mSelections.remove(Integer.valueOf(position));
        }
        notifyItemChanged(position);
    }

    public List<Item> deleteAllSelectedItems() {
        List<Item> deletedItems = new LinkedList();
        if (this.mPositionBasedStateManagement) {
            Set<Integer> selections = getSelections();
            while (selections.size() > 0) {
                Iterator<Integer> iterator = selections.iterator();
                int position = ((Integer) iterator.next()).intValue();
                IAdapter adapter = getAdapter(position);
                if (adapter == null || !(adapter instanceof IItemAdapter)) {
                    iterator.remove();
                } else {
                    deletedItems.add(getItem(position));
                    ((IItemAdapter) adapter).remove(position);
                }
                selections = getSelections();
            }
        } else {
            for (int i = getItemCount() - 1; i >= 0; i--) {
                RelativeInfo<Item> ri = getRelativeInfo(i);
                if (ri.item.isSelected() && ri.adapter != null && (ri.adapter instanceof IItemAdapter)) {
                    ((IItemAdapter) ri.adapter).remove(i);
                }
            }
        }
        return deletedItems;
    }

    public SparseIntArray getExpanded() {
        if (this.mPositionBasedStateManagement) {
            return this.mExpanded;
        }
        SparseIntArray expandedItems = new SparseIntArray();
        int length = getItemCount();
        for (int i = 0; i < length; i++) {
            Item item = getItem(i);
            if ((item instanceof IExpandable) && ((IExpandable) item).isExpanded()) {
                expandedItems.put(i, ((IExpandable) item).getSubItems().size());
            }
        }
        return expandedItems;
    }

    public int[] getExpandedItems() {
        int[] expandedItems;
        int length;
        int i;
        if (this.mPositionBasedStateManagement) {
            length = this.mExpanded.size();
            expandedItems = new int[length];
            for (i = 0; i < length; i++) {
                expandedItems[i] = this.mExpanded.keyAt(i);
            }
        } else {
            ArrayList<Integer> expandedItemsList = new ArrayList();
            length = getItemCount();
            for (i = 0; i < length; i++) {
                Item item = getItem(i);
                if ((item instanceof IExpandable) && ((IExpandable) item).isExpanded()) {
                    expandedItemsList.add(Integer.valueOf(i));
                }
            }
            int expandedItemsListLength = expandedItemsList.size();
            expandedItems = new int[expandedItemsListLength];
            for (i = 0; i < expandedItemsListLength; i++) {
                expandedItems[i] = ((Integer) expandedItemsList.get(i)).intValue();
            }
        }
        return expandedItems;
    }

    public void toggleExpandable(int position) {
        if (!this.mPositionBasedStateManagement) {
            Item item = getItem(position);
            if ((item instanceof IExpandable) && ((IExpandable) item).isExpanded()) {
                collapse(position);
            } else {
                expand(position);
            }
        } else if (this.mExpanded.indexOfKey(position) >= 0) {
            collapse(position);
        } else {
            expand(position);
        }
    }

    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean notifyItemChanged) {
        int[] expandedItems = getExpandedItems();
        for (int i = expandedItems.length - 1; i >= 0; i--) {
            collapse(expandedItems[i], notifyItemChanged);
        }
    }

    public void collapse(int position) {
        collapse(position, false);
    }

    public void collapse(int position, boolean notifyItemChanged) {
        Item item = getItem(position);
        if (item != null && (item instanceof IExpandable)) {
            IExpandable expandable = (IExpandable) item;
            if (expandable.isExpanded() && expandable.getSubItems() != null && expandable.getSubItems().size() > 0) {
                int totalAddedItems;
                int i;
                if (this.mPositionBasedStateManagement) {
                    totalAddedItems = expandable.getSubItems().size();
                    int length = this.mExpanded.size();
                    i = 0;
                    while (i < length) {
                        if (this.mExpanded.keyAt(i) > position && this.mExpanded.keyAt(i) <= position + totalAddedItems) {
                            totalAddedItems += this.mExpanded.get(this.mExpanded.keyAt(i));
                        }
                        i++;
                    }
                    Iterator<Integer> selectionsIterator = this.mSelections.iterator();
                    while (selectionsIterator.hasNext()) {
                        Integer value = (Integer) selectionsIterator.next();
                        if (value.intValue() > position && value.intValue() <= position + totalAddedItems) {
                            deselect(value.intValue(), selectionsIterator);
                        }
                    }
                    i = length - 1;
                    while (i >= 0) {
                        if (this.mExpanded.keyAt(i) > position && this.mExpanded.keyAt(i) <= position + totalAddedItems) {
                            totalAddedItems -= this.mExpanded.get(this.mExpanded.keyAt(i));
                            internalCollapse(this.mExpanded.keyAt(i), notifyItemChanged);
                        }
                        i--;
                    }
                    internalCollapse(expandable, position, notifyItemChanged);
                    return;
                }
                Item tmp;
                IExpandable tmpExpandable;
                totalAddedItems = expandable.getSubItems().size();
                for (i = position + 1; i < position + totalAddedItems; i++) {
                    tmp = getItem(i);
                    if (tmp instanceof IExpandable) {
                        tmpExpandable = (IExpandable) tmp;
                        if (tmpExpandable.getSubItems() != null && tmpExpandable.isExpanded()) {
                            totalAddedItems += tmpExpandable.getSubItems().size();
                        }
                    }
                }
                i = (position + totalAddedItems) - 1;
                while (i > position) {
                    tmp = getItem(i);
                    if (tmp instanceof IExpandable) {
                        tmpExpandable = (IExpandable) tmp;
                        if (tmpExpandable.isExpanded()) {
                            collapse(i);
                            if (tmpExpandable.getSubItems() != null) {
                                i -= tmpExpandable.getSubItems().size();
                            }
                        }
                    }
                    i--;
                }
                internalCollapse(expandable, position, notifyItemChanged);
            }
        }
    }

    private void internalCollapse(int position, boolean notifyItemChanged) {
        Item item = getItem(position);
        if (item != null && (item instanceof IExpandable)) {
            IExpandable expandable = (IExpandable) item;
            if (expandable.isExpanded() && expandable.getSubItems() != null && expandable.getSubItems().size() > 0) {
                internalCollapse(expandable, position, notifyItemChanged);
            }
        }
    }

    private void internalCollapse(IExpandable expandable, int position, boolean notifyItemChanged) {
        IAdapter adapter = getAdapter(position);
        if (adapter != null && (adapter instanceof IItemAdapter)) {
            ((IItemAdapter) adapter).removeRange(position + 1, expandable.getSubItems().size());
        }
        expandable.withIsExpanded(false);
        if (this.mPositionBasedStateManagement) {
            int indexOfKey = this.mExpanded.indexOfKey(position);
            if (indexOfKey >= 0) {
                this.mExpanded.removeAt(indexOfKey);
            }
        }
        if (notifyItemChanged) {
            notifyItemChanged(position);
        }
    }

    public void expand(int position) {
        expand(position, false);
    }

    public void expand(int position, boolean notifyItemChanged) {
        Item item = getItem(position);
        if (item != null && (item instanceof IExpandable)) {
            IExpandable<?, Item> expandable = (IExpandable) item;
            IAdapter<Item> adapter;
            if (this.mPositionBasedStateManagement) {
                if (this.mExpanded.indexOfKey(position) < 0 && expandable.getSubItems() != null && expandable.getSubItems().size() > 0) {
                    int size;
                    adapter = getAdapter(position);
                    if (adapter != null && (adapter instanceof IItemAdapter)) {
                        ((IItemAdapter) adapter).add(position + 1, expandable.getSubItems());
                    }
                    expandable.withIsExpanded(true);
                    if (notifyItemChanged) {
                        notifyItemChanged(position);
                    }
                    SparseIntArray sparseIntArray = this.mExpanded;
                    if (expandable.getSubItems() != null) {
                        size = expandable.getSubItems().size();
                    } else {
                        size = 0;
                    }
                    sparseIntArray.put(position, size);
                }
            } else if (!expandable.isExpanded() && expandable.getSubItems() != null && expandable.getSubItems().size() > 0) {
                adapter = getAdapter(position);
                if (adapter != null && (adapter instanceof IItemAdapter)) {
                    ((IItemAdapter) adapter).add(position + 1, expandable.getSubItems());
                }
                expandable.withIsExpanded(true);
                if (notifyItemChanged) {
                    notifyItemChanged(position);
                }
            }
        }
    }

    public void notifyAdapterDataSetChanged() {
        if (this.mPositionBasedStateManagement) {
            this.mSelections.clear();
            this.mExpanded.clear();
        }
        cacheSizes();
        notifyDataSetChanged();
        if (this.mPositionBasedStateManagement) {
            AdapterUtil.handleStates(this, 0, getItemCount() - 1);
        }
    }

    public void notifyAdapterItemInserted(int position) {
        notifyAdapterItemRangeInserted(position, 1);
    }

    public void notifyAdapterItemRangeInserted(int position, int itemCount) {
        if (this.mPositionBasedStateManagement) {
            this.mSelections = AdapterUtil.adjustPosition(this.mSelections, position, Integer.MAX_VALUE, itemCount);
            this.mExpanded = AdapterUtil.adjustPosition(this.mExpanded, position, Integer.MAX_VALUE, itemCount);
        }
        cacheSizes();
        notifyItemRangeInserted(position, itemCount);
        if (this.mPositionBasedStateManagement) {
            AdapterUtil.handleStates(this, position, (position + itemCount) - 1);
        }
    }

    public void notifyAdapterItemRemoved(int position) {
        notifyAdapterItemRangeRemoved(position, 1);
    }

    public void notifyAdapterItemRangeRemoved(int position, int itemCount) {
        if (this.mPositionBasedStateManagement) {
            this.mSelections = AdapterUtil.adjustPosition(this.mSelections, position, Integer.MAX_VALUE, itemCount * -1);
            this.mExpanded = AdapterUtil.adjustPosition(this.mExpanded, position, Integer.MAX_VALUE, itemCount * -1);
        }
        cacheSizes();
        notifyItemRangeRemoved(position, itemCount);
    }

    public void notifyAdapterItemMoved(int fromPosition, int toPosition) {
        collapse(fromPosition);
        collapse(toPosition);
        if (this.mPositionBasedStateManagement) {
            if (!this.mSelections.contains(Integer.valueOf(fromPosition)) && this.mSelections.contains(Integer.valueOf(toPosition))) {
                this.mSelections.remove(Integer.valueOf(toPosition));
                this.mSelections.add(Integer.valueOf(fromPosition));
            } else if (this.mSelections.contains(Integer.valueOf(fromPosition)) && !this.mSelections.contains(Integer.valueOf(toPosition))) {
                this.mSelections.remove(Integer.valueOf(fromPosition));
                this.mSelections.add(Integer.valueOf(toPosition));
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void notifyAdapterItemChanged(int position) {
        notifyAdapterItemChanged(position, null);
    }

    public void notifyAdapterItemChanged(int position, Object payload) {
        notifyAdapterItemRangeChanged(position, 1, payload);
    }

    public void notifyAdapterItemRangeChanged(int position, int itemCount) {
        notifyAdapterItemRangeChanged(position, itemCount, null);
    }

    public void notifyAdapterItemRangeChanged(int position, int itemCount, Object payload) {
        for (int i = position; i < position + itemCount; i++) {
            if (!this.mPositionBasedStateManagement) {
                Item item = getItem(position);
                if ((item instanceof IExpandable) && ((IExpandable) item).isExpanded()) {
                    collapse(position);
                }
            } else if (this.mExpanded.indexOfKey(i) >= 0) {
                collapse(i);
            }
        }
        if (payload == null) {
            notifyItemRangeChanged(position, itemCount);
        } else {
            notifyItemRangeChanged(position, itemCount, payload);
        }
        if (this.mPositionBasedStateManagement) {
            AdapterUtil.handleStates(this, position, (position + itemCount) - 1);
        }
    }

    public void notifyAdapterSubItemsChanged(int position) {
        if (!this.mPositionBasedStateManagement) {
            Log.e("FastAdapter", "please use the notifyAdapterSubItemsChanged(int position, int previousCount) method instead in the PositionBasedStateManagement mode, as we are not able to calculate the previous count ");
        } else if (this.mExpanded.indexOfKey(position) > -1) {
            this.mExpanded.put(position, notifyAdapterSubItemsChanged(position, this.mExpanded.get(position)));
        }
    }

    public int notifyAdapterSubItemsChanged(int position, int previousCount) {
        Item item = getItem(position);
        if (item == null || !(item instanceof IExpandable)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        IAdapter adapter = getAdapter(position);
        if (adapter != null && (adapter instanceof IItemAdapter)) {
            ((IItemAdapter) adapter).removeRange(position + 1, previousCount);
            ((IItemAdapter) adapter).add(position + 1, expandable.getSubItems());
        }
        return expandable.getSubItems().size();
    }
}
