package com.github.tongca.pattern;

/**
 * Created by Toan Nguyen Canh
 * NVG 7/20/15
 */
public abstract class HistoryList<ID, T extends IModel<ID>> extends ExpandedList<ID, T> {
    private int capacity = 0;

    @Override
    public T get(int position) {
        return super.get(size() - 1 - position);
    }

    @Override
    protected void add(T object, boolean cacheOnDisk, boolean cacheOnMemory) {
        super.add(object, cacheOnDisk, cacheOnMemory);
        if (capacity > 0 && size() > capacity) {
            trim();
        }

    }

    @Override
    protected void onDataSetChanged() {
        trim();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    protected final void trim() {
        while (capacity > 0 && size() > capacity) {
            remove(super.get(0).getId());
        }
    }
}
