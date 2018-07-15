package com.jay.vito.website.core.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheDataModel {

	private Map<String, Object> cacheMap = new HashMap<String, Object>();
	
	public Object get(String key) {
		return cacheMap.get(key);
	}

	public void add(String key, Object value) {
		cacheMap.remove(key);
		cacheMap.put(key, value);
	}
	
}
