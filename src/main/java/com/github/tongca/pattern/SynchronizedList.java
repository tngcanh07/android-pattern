package com.github.tongca.pattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public abstract class SynchronizedList<ID, T extends IModel<ID>> extends
		Observable {
	private IDataCache<ID, T> memoryCache;
	private IDataCache<ID, T> diskCache;

	private final ArrayList<ID> arrIds = new ArrayList<>();

	private final Set<ID> setIds = new HashSet<>();

	/**
	 * constructors
	 */
	public SynchronizedList() {
		memoryCache = new JMMemoryCache<>();
	}

	public void sync() {

	}

	public void add(List<T> objects) {
		for (T object : objects) {
			add(object);
		}
	}

	protected void addIds(List<ID> ids) {
		for (ID id : ids) {
			addId(id);
		}
	}

	protected void addId(ID id) {
		arrIds.add(id);
		setIds.add(id);
	}

	public void add(T[] objects) {
		for (T object : objects) {
			add(object);
		}
	}

	public void add(T object) {
		if (!contain(object.getId())) {
			arrIds.add(object.getId());
			setIds.add(object.getId());
			setChanged();
		}
		if (memoryCache != null) {
			memoryCache.put(object);
		}
		if (diskCache != null) {
			diskCache.put(object);
		}
	}

	public void remove(ID id) {
		arrIds.remove(id);
		setIds.remove(id);
		if (memoryCache != null) {
			memoryCache.delete(id);
		}
		if (diskCache != null) {
			diskCache.delete(id);
		}
		setChanged();
	}

	public void clear(boolean clearMemoryCache, boolean clearDiskCache) {
		arrIds.clear();
		setIds.clear();
		if (clearMemoryCache) {
			clearMemoryCache();
		}
		if (clearDiskCache) {
			clearDiskCache();
		}
	}

	public void clearMemoryCache() {
		if (memoryCache != null) {
			memoryCache.clear();
		}
	}

	public void clearDiskCache() {
		if (diskCache != null) {
			diskCache.clear();
		}
	}

	public int getCount() {
		return arrIds != null ? arrIds.size() : 0;
	}

	public boolean contain(ID id) {
		return setIds.contains(id);
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

	public void setMemoryCache(IDataCache<ID, T> memoryCache) {
		this.memoryCache = memoryCache;
	}

	public void setDiskCache(IDataCache<ID, T> diskCache) {
		this.diskCache = diskCache;
	}

}
