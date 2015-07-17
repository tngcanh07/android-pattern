package com.github.tongca.pattern;

import java.util.List;

public interface IDataCache<ID, T extends IModel<ID>> {
    T get(ID id);

    List<T> get();

    T put(T object);

    boolean delete(ID id);

    boolean delete();
}
