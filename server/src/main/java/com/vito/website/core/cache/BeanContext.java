package com.vito.website.core.cache;

import java.util.Map;

/**
 * <p>
 * Title: 应用服务工具平台-
 * </p>
 * 
 * <p>
 * Description: 应用容器接口
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * @author ChenTao
 * @version 1.0
 */
public interface BeanContext {
	
	public Object getBean(String beanName);
	
	<T> Map<String, T> getBeansByType(Class<T> clazz);
	
	<T> T getBean(String beanName, Class<T> clazz);
	
	<T> T getBean(Class<T> clazz);

	public void reload();

	public void autowireComponent(Object bean);
}
