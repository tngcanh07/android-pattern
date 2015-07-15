package com.github.tongca.pattern;

public interface IDataCache<ID, T extends IModel<ID>> {
	T get(ID id);

	T put(T object);

	void delete(ID id);

	void clear();

}
