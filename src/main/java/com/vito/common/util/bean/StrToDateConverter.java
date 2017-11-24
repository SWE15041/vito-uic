package com.vito.common.util.bean;

import com.vito.common.util.date.DateUtil;
import org.apache.commons.beanutils.Converter;

/**
 * 自定义的java.util.Date的类型转换器，注册后供BeanUtils使用
 *
 */
public class StrToDateConverter implements Converter {

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	@Override
	public Object convert(Class targetClass, Object value) {
		Object obj = value;
		if(java.util.Date.class == targetClass){
			if(value != null && value instanceof String){
				if (!DateUtil.checkDateStr((String) value)) {
					throw new ClassCastException("时间字符串格式不正确，无法将字符串转换为Date类型");
				}
				obj = DateUtil.changeStrToDate((String) value);
			}
		}
		return obj;
	}

}