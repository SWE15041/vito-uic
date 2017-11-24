package com.vito.common.util.date;

import java.sql.Timestamp;

public class DatabaseTimeUtil {

	/**
	 * 获取数据库日期时间格式的当前时间
	 * 
	 * @return
	 */
	public static Timestamp getNow() {
		return new Timestamp(new java.util.Date().getTime());
	}
	
	/**
	 * 获取java.util.Date对应的sql time类型
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp getTime(java.util.Date date) {
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}
	
}
