package com.github.tongca.pattern;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LruMemoryCache<ID, T extends IModel<ID>> implements
        IDataProvider<ID, T> {

    private LruCache<ID, T> memoryCache;

    public LruMemoryCache(int capital) {
        memoryCache = new LruCache<>(capital);
    }

    @Override
    public List<T> get() {
        // doesn't support
        return new ArrayList<>();
    }

    @Override
    public T get(ID id) {
        return memoryCache.get(id);
    }

    @Override
    public void put(T object) {
        memoryCache.put(object.getId(), object);
    }

    @Override
    public void clear() {
        memoryCache.evictAll();
    }

    @Override
    public boolean remove(ID id) {
        return memoryCache.remove(id) != null;
    }

    @Override
    public int remove(Set<ID> ids) {
        int result = 0;
        for (ID id : ids) {
            if (remove(id))
                result++;
        }
        return result;
    }
}
