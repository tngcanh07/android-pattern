package com.github.tongca.pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SynchronizedList<ID, T extends IModel<ID>> {
    private final ArrayList<ID> arrIds = new ArrayList<>();
    boolean isChanged = false;
    private IDataProvider<ID, T> memoryCache;
    private IDataProvider<ID, T> diskCache;

    /**
     * constructors
     */
    public SynchronizedList() {
        memoryCache = new MemoryCache<>();
    }

    protected void initFromDiskCache() {
        if (diskCache != null) {
            add(diskCache.get(), false, true);
            if (isChanged) {
                isChanged = false;
                onDataSetChanged();
            }
        }
    }

    protected void add(List<T> objects, boolean cacheOnDisk, boolean cacheOnMemory) {
        for (T object : objects) {
            add(object, cacheOnDisk, cacheOnMemory);
        }
    }

    protected void add(T[] objects, boolean cacheOnDisk, boolean cacheOnMemory) {
        for (T object : objects) {
            add(object, cacheOnDisk, cacheOnMemory);
        }
    }

    protected void add(T object, boolean cacheOnDisk, boolean cacheOnMemory) {
        if (object == null) {
            return;
        }
        if (shouldUpdate(object)) {
            if (cacheOnMemory && memoryCache != null) {
                memoryCache.put(object);
            }
            if (cacheOnDisk && diskCache != null) {
                diskCache.put(object);
            }

            if (!contain(object.getId())) {
                arrIds.add(object.getId());
                onDataSetChanged();
            }
        }
    }

    public boolean shouldUpdate(T object) {
        Date newDate = object.getModifiedDate();
        Date oldDate = null;
        if (contain(object.getId())) {
            oldDate = getById(object.getId()).getModifiedDate();
        }
        if (oldDate != null && newDate != null) {
            return newDate.before(oldDate);
        }
        return oldDate == null;
    }

    public void remove(ID id) {
        if (memoryCache != null) {
            memoryCache.delete(id);
        }
        if (diskCache != null) {
            diskCache.delete(id);
        }
        if (contain(id)) {
            arrIds.remove(id);
        }
        onDataSetChanged();
    }

    public void clear(boolean clearMemoryCache, boolean clearDiskCache) {
        arrIds.clear();
        if (clearMemoryCache) {
            clearMemoryCache();
        }
        if (clearDiskCache) {
            clearDiskCache();
        }
    }

    public void clearMemoryCache() {
        if (memoryCache != null) {
            memoryCache.delete();
        }
    }

    public void clearDiskCache() {
        if (diskCache != null) {
            diskCache.delete();
        }
    }

    /**
     * get the number of favorite objects
     *
     * @return number of favorite objectsF
     */
    public int getCount() {
        return arrIds.size();
    }

    public boolean contain(ID id) {
        return arrIds.contains(id);
    }

    public T get(int position) {
        return getById(arrIds.get(position));
    }

    public T getById(ID id) {
        if (!contain(id))
            return null;
        T object = null;
        if (memoryCache != null) {
            object = memoryCache.get(id);
        }
        if (object == null && diskCache != null) {
            object = diskCache.get(id);
            if (object != null && memoryCache != null) {
                memoryCache.put(object);
            }
        }

        return object;
    }

    public int indexOf(ID id) {
        if (!contain(id))
            return -1;
        return arrIds.indexOf(id);
    }

    protected T getFromMemoryCache(ID id) {
        T object = null;
        if (memoryCache != null) {
            object = memoryCache.get(id);
        }
        return object;
    }

    public void setMemoryCache(IDataProvider<ID, T> memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void setDiskCache(IDataProvider<ID, T> diskCache) {
        this.diskCache = diskCache;
    }

    /**
     * this method will be called every time when data change (add/remove)
     */
    protected abstract void onDataSetChanged();
}
