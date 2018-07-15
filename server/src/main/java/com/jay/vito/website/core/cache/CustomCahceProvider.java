package com.jay.vito.website.core.cache;

/**
 * 自定义缓存提供器接口
 * 
 * @author zhaixm
 *
 */
public interface CustomCahceProvider {
	
	/**
	 * 向用户自定义缓存中添加缓存数据
	 * 
	 * @param customChacheData
	 */
	void addCacheData(CacheDataModel customChacheData);
	
}
