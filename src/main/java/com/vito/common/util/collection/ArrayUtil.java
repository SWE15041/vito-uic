package com.vito.common.util.collection;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ArrayUtil {
	
	/**
	 * 描述：字符串切割转化成List
	 * @param array<String> 需要转化的字符串 
	 * @param split<String> 字符串分隔符
	 */
	public static List changeArrayToArrayList(String arrayStr, String split){
		List resultList = new ArrayList();
		if (arrayStr != null && !"".equals(arrayStr)) {
			resultList = Arrays.asList(arrayStr.split(split));
		}
		return resultList;
	}
	
	/**
	 * 描述：字符串切割转化成Set
	 * @param array<String> 需要转化的字符串 
	 * @param split<String> 字符串分隔符
	 */
	public static Set changeArrayToArraySet(String arrayStr, String split){
		Set resultSet = new HashSet();
		if (arrayStr != null && !"".equals(arrayStr)) {
			String[] array = arrayStr.split(split);
			for(int i=0, length=array.length; i<length; i++){
				resultSet.add(array[i]);
			}
		}
		return resultSet;
	}

}
