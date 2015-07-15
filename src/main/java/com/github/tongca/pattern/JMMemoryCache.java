package com.github.tongca.pattern;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class JMMemoryCache<ID, T extends IModel<ID>> implements
		IDataCache<ID, T> {
	private final HashMap<ID, Object> cachedObjects;

	public JMMemoryCache() {
		cachedObjects = new HashMap<>();
	}

	@Override
	public T get(ID id) {
		return (T) cachedObjects.get(id);
	}

	@Override
	public T put(T object) {
		cachedObjects.put(object.getId(), object);
		return object;
	}

	@Override
	public void delete(ID id) {
		cachedObjects.remove(id);
	}

	@Override
	public void clear() {
		cachedObjects.clear();
	}

}
