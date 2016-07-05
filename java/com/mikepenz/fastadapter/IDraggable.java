package com.mikepenz.fastadapter;

public interface IDraggable<T, Item extends IItem> {
    boolean isDraggable();

    T withIsDraggable(boolean z);
}
