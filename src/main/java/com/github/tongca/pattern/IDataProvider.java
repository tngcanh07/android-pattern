package com.github.tongca.pattern;

import java.util.Date;
import java.util.List;

/**
 * Created by tongca on 7/14/2015.
 */
public interface IDataProvider<ID, T extends IModel<ID>> {
    List<T> get(int page, int pageSize, Date lastModified);
    int count(Date lastModified);
    void insertOrUpdate(T object);
    T get(ID id);
}
