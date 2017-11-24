package com.vito.common.util.bean;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
@SuppressWarnings({"rawtypes"})
public class BeanUtilBean extends BeanUtilsBean {
	
	private static Logger logger = Logger.getLogger(BeanUtilBean.class);
	
	private static final ContextClassLoaderLocal beansByClassLoader = new ContextClassLoaderLocal() {

		protected Object initialValue() {
			return new BeanUtilBean();
		}

	};

	public static synchronized BeanUtilBean getInstance() {
		return (BeanUtilBean) beansByClassLoader.get();
	}
	
	public void copyNotNullProperties(Object dest, Object orig)
			throws IllegalAccessException, InvocationTargetException {
		if (dest == null)
			throw new IllegalArgumentException("No destination bean specified");
		if (orig == null)
			throw new IllegalArgumentException("No origin bean specified");
		if (logger.isDebugEnabled())
			logger.debug("BeanUtils.copyProperties(" + dest + ", " + orig + ")");
		if (orig instanceof DynaBean) {
			DynaProperty origDescriptors[] = ((DynaBean) orig).getDynaClass()
					.getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if (getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((DynaBean) orig).get(name);
					if(value != null){
						copyProperty(dest, name, value);
					}
				}
			}

		} else if (orig instanceof Map) {
			for (Iterator names = ((Map) orig).keySet().iterator(); names
					.hasNext();) {
				String name = (String) names.next();
				if (getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((Map) orig).get(name);
					if(value != null){
						copyProperty(dest, name, value);
					}
				}
			}

		} else {
			PropertyDescriptor origDescriptors[] = getPropertyUtils()
					.getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if (!"class".equals(name)
						&& getPropertyUtils().isReadable(orig, name)
						&& getPropertyUtils().isWriteable(dest, name))
					try {
						Object value = getPropertyUtils().getSimpleProperty(
								orig, name);
						if(value != null){
							copyProperty(dest, name, value);
						}
					} catch (NoSuchMethodException e) {
					}
			}

		}
	}
}
