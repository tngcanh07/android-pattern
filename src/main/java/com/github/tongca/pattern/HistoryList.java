package com.github.tongca.pattern;

/**
 * Created by Toan Nguyen Canh
 * NVG 7/20/15
 */
public abstract class HistoryList<ID, T extends IModel<ID>> extends SynchronizedList<ID, T> {
    private int capacity = 0;

    @Override
    public T get(int position) {
        return super.get(getCount() - 1 - position);
    }

    @Override
    protected void add(T object, boolean cacheOnDisk, boolean cacheOnMemory) {
        super.add(object, cacheOnDisk, cacheOnMemory);
        if (capacity > 0 && getCount() > capacity) {
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
        while (getCount() > capacity) {
            remove(super.get(0).getId());
        }
    }
}
