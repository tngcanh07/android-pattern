package com.github.tongca.pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SynchronizedList<ID, T extends IModel<ID>> {
    private final ArrayList<ID> arrIds = new ArrayList<>();
    private IDataProvider<ID, T> memoryCache;
    private IDataProvider<ID, T> diskCache;
    private long revision = 0;

    public long getRevision() {
        return revision;
    }

    /**
     * constructors
     */
    public SynchronizedList() {
        memoryCache = new MemoryCache<>();
    }

    protected void initFromDiskCache() {
        if (diskCache != null) {
            add(diskCache.get(), false, true);
        }
    }

    protected void add(List<T> objects, boolean cacheOnDisk, boolean cacheOnMemory) {
        long rev = getRevision();
        for (T object : objects) {
            if (addItem(object, cacheOnDisk, cacheOnMemory)) {
                onItemAdded(object);
            }
        }
        if (rev < getRevision())
            onDataSetChanged();
    }

    protected void add(T[] objects, boolean cacheOnDisk, boolean cacheOnMemory) {
        long rev = getRevision();
        for (T object : objects) {
            if (addItem(object, cacheOnDisk, cacheOnMemory)) {
                onItemAdded(object);
            }
        }
        if (rev < getRevision())
            onDataSetChanged();
    }

    protected void add(T object, boolean cacheOnDisk, boolean cacheOnMemory) {
        if (addItem(object, cacheOnDisk, cacheOnMemory)) {
            onItemAdded(object);
            onDataSetChanged();
        }
    }

    private boolean addItem(T object, boolean cacheOnDisk, boolean cacheOnMemory) {
        if (object == null) {
            return false;
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
            }
            revision++;
            return true;
        }
        return false;
    }

    private boolean shouldUpdate(T object) {
        try {
            Date newDate = object.getModifiedDate();
            Date oldDate = null;
            if (contain(object.getId())) {
                oldDate = getById(object.getId()).getModifiedDate();
            }
            if (oldDate != null && newDate != null) {
                return newDate.before(oldDate);
            }
            return oldDate == null;
        } catch (Exception e) {
            return true;
        }
    }

    public void remove(ID id) {
        remove(id, true, true);
    }

    protected void remove(ID id, boolean removeOnDisk, boolean removeOnMemory) {
        final T object = getById(id);
        if (removeItem(id, removeOnDisk, removeOnMemory) && object != null) {
            onItemDeleted(object);
            onDataSetChanged();
        }
    }

    public void clear(boolean clearMemoryCache, boolean clearDiskCache) {
        arrIds.clear();
        if (clearMemoryCache) {
            clearMemoryCache();
        }
        if (clearDiskCache) {
            clearDiskCache();
        }
        onDataSetChanged();
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

    public void enableMemoryCache(IDataProvider<ID, T> memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void enableDiskCache(IDataProvider<ID, T> diskCache) {
        this.diskCache = diskCache;
    }

    /**
     * this method will be called every time when data change (add/remove)
     */

    protected void onItemAdded(T object) {

    }

    protected void onItemDeleted(T object) {

    }

    protected void onDataSetChanged() {

    }

    private boolean removeItem(ID id, boolean removeOnDisk, boolean removeOnMemory) {
        if (removeOnMemory && memoryCache != null) {
            memoryCache.delete(id);
        }
        if (removeOnDisk && diskCache != null) {
            diskCache.delete(id);
        }
        if (contain(id)) {
            arrIds.remove(id);
            revision++;
            return true;
        }
        return false;
    }
}
