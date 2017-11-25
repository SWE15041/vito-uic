package com.vito.common.util.bean;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtil extends BeanUtils {

	private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	static {
		ConvertUtils.register(new StrToDateConverter(), java.util.Date.class);
	}
	
	/**
	 * 将源对象中的非空属性值拷贝到目标对象中
	 * 
	 * @param dest
	 * @param orig
	 */
	public static void copyNotNullProperties(Object dest, Object orig){
		try {
			BeanUtilBean.getInstance().copyNotNullProperties(dest, orig);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public static void copyProperties(Object dest, Object orig){
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
