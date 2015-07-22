package com.github.tongca.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MemoryCache<ID, T extends IModel<ID>> implements
        IDataProvider<ID, T> {
    private final HashMap<ID, T> cachedObjects;

    public MemoryCache() {
        cachedObjects = new HashMap<>();
    }

    @Override
    public T get(ID id) {
        return cachedObjects.get(id);
    }

    @Override
    public List<T> get() {
        ArrayList<T> all = new ArrayList<>();
        Set<ID> ids = cachedObjects.keySet();
        for (ID id : ids) {
            all.add(cachedObjects.get(id));
        }
        return all;
    }

    @Override
    public T put(T object) {
        cachedObjects.put(object.getId(), object);
        return object;
    }

    @Override
    public boolean delete(ID id) {
        cachedObjects.remove(id);
        return true;
    }

    @Override
    public boolean delete() {
        cachedObjects.clear();
        return true;
    }

}
