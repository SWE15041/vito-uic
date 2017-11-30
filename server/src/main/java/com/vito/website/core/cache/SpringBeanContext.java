package com.vito.website.core.cache;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * <p>
 * Title: 应用服务工具平台-SpringContainer
 * </p>
 * 
 * <p>
 * Description: Spring容器类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * @author ChenTao
 * @version 1.0
 */
public class SpringBeanContext implements BeanContext {
	private ApplicationContext applicationContext;

	public SpringBeanContext(ServletContext servletContext) {
		this.applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
	}

	public SpringBeanContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void autowireComponent(Object bean) {
		((AbstractApplicationContext) applicationContext).getBeanFactory()
                                                         .autowireBeanProperties(bean,
						AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}
	
	@Override
	public <T> Map<String, T> getBeansByType(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz);
	}

	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	@Override
	public <T> T getBean(String beanName, Class<T> clazz) {
		if(applicationContext.getBean(beanName) != null) {
			return clazz.cast(applicationContext.getBean(beanName));
		} else {
			return null;
		}
	}

	public void reload() {
		close();
		((AbstractApplicationContext) applicationContext).refresh();
	}

	public void close() {
		((AbstractApplicationContext) applicationContext).close();
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
}
