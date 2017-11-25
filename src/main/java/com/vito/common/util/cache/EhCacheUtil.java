package com.vito.common.util.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EhCacheUtil {
	private static Logger logger = LoggerFactory.getLogger(EhCacheUtil.class);

	/**
	 * 缓存管理器
	 */
	private static CacheManager manager;

	public static CacheManager getManager() {
		if (manager == null) {
			init();
		}
		return manager;
	}

	/**
	 * 初始化缓存管理器
	 * 
	 * @author:Administrator
	 */
	public static synchronized void init() {
		try {
			if (manager == null) {
				manager = CacheManager.create();
				logger.info("缓存器初始化成功");
			}
		} catch (CacheException e) {
			logger.error("缓存器初始化失败");
			e.printStackTrace();
		}
	}

	/** 销毁 */
	public static synchronized void destroy() {
		manager.shutdown();
		manager = null;
		logger.info("缓存销毁成功");
	}

	/**
	 * 
	 * 
	 * @author:Administrator
	 */
	public static synchronized void reset() {
		logger.info("缓存刷新成功");
		if (manager == null) {
			init();
		} else {
			destroy();
			init();
		}
	}

}
