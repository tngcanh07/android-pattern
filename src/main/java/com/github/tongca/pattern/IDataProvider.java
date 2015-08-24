package com.github.tongca.pattern;

import java.util.List;
import java.util.Set;

public interface IDataProvider<ID, T extends IModel<ID>> {
    T get(ID id);

    List<T> get();

    void put(T object);

    void clear();

    boolean remove(ID id);

    int remove(Set<ID> ids);

}
