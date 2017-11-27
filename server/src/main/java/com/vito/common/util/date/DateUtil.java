/**
 * Copyright© 2009 Xiamen Dragon Software Eng. Co. Ltd.
 * All right reserved. 
 */
package com.vito.common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * <br>
 * <br>
 * <p>
 * ====================================================<br>
 * 修订记录<br>
 * 日期 作者 操作<br>
 * Oct 22, 2009 zhaixm 创建<br>
 * ====================================================<br>
 * <p>
 * 
 * @author zhaixm
 * @since 1.0
 * @see
 */
public class DateUtil {
	
	/** 数据库存储的时间格式串，时间数字串 */
	public static final int DB_STORE_DATE_SPLIT = 1;
	/** 数据库存储的时间格式串，如yyyyMMdd */
	public static final String DB_STORE_DATE = "yyyyMMdd";
	/** 数据库存储的时间格式串，如yyyyMMddHHMiSS */
	public static final String DB_STORE_DATE_FULL = "yyyyMMddHHmmss";
	/** 数据库存储的时间格式串，精确到毫秒，如yyyyMMddHHmmssSS */
	public static final String DB_STORE_DATE_FULL_MILLI = "yyyyMMddHHmmssSS";
	/** 数据库存储的时间格式串，如yyyyMM */
	public static final String DB_STORE_DATE_MONTH = "yyyyMM";

	/** 用连字符-分隔的时间格式串 */
	public static final int LINK_DISPLAY_DATE_SPLIT = 2;
	/** 用连字符-分隔的时间格式串，如yyyy-MM-dd */
	public static final String LINK_DISPLAY_DATE = "yyyy-MM-dd";
	/** 用连字符-分隔的时间格式串，如yyyy-MM-dd HH:Mi:SS */
	public static final String LINK_DISPLAY_DATE_FULL = "yyyy-MM-dd HH:mm:ss";
	/** 用连字符-分隔的时间格式串，如yyyy-MM */
	public static final String LINK_DISPLAY_DATE_MONTH = "yyyy-MM";

	/** 用连字符.分隔的时间格式串 */
	public static final int DOT_DISPLAY_DATE_SPLIT = 3;
	/** 用连字符.分隔的时间格式串，如yyyy.MM.dd */
	public static final String DOT_DISPLAY_DATE = "yyyy.MM.dd";
	/** 用连字符.分隔的时间格式串，如yyyy.MM.dd HH:Mi:SS */
	public static final String DOT_DISPLAY_DATE_FULL = "yyyy.MM.dd HH:mm:ss";
	/** 用连字符.分隔的时间格式串，如yyyy.MM */
	public static final String DOT_DISPLAY_DATE_MONTH = "yyyy.MM";

	/** 用中文字符分隔的时间格式串 */
	public static final int CN_DISPLAY_DATE_SPLIT = 4;
	/** 用中文字符分隔的时间格式串，如yyyy年MM月dd日 */
	public static final String CN_DISPLAY_DATE = "yyyy年MM月dd日";
	/** 用中文字符分隔的时间格式串，如yyyy年MM月dd日 HH:Mi:SS */
	public static final String CN_DISPLAY_DATE_FULL = "yyyy年MM月dd日 HH:mm:ss";
	/** 用中文字符分隔的时间格式串，如yyyy年MM月 */
	public static final String CN_DISPLAY_DATE_MONTH = "yyyy年MM月";

	public static int getDaysBetween(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		if (c1.after(c2)) {
			Calendar swap = c1;
			c1 = c2;
			c2 = swap;
		}
		int days = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		int y2 = c2.get(Calendar.YEAR);
		if (c1.get(Calendar.YEAR) != y2) {
			c1 = (Calendar) c1.clone();
			do {
				days += c1.getActualMaximum(Calendar.DAY_OF_YEAR);
				c1.add(Calendar.YEAR, 1);
			} while (c1.get(Calendar.YEAR) != y2);
		}
		return days;
	}
	
	/**
	 * 根据相应的长度，及格式化分隔符返回日期类型
	 * @param formatSplitType
	 * @param length
	 * @return
	 * @author:chenxy
	 */
	private static String getFormatType(int formatSplitType, int length) {
		String formatStr = null;
		switch (formatSplitType) {
			case DateUtil.LINK_DISPLAY_DATE_SPLIT:
				switch (length) {
					case 14:
						formatStr = DateUtil.LINK_DISPLAY_DATE_FULL;
						break;
					case 6:
						formatStr = DateUtil.LINK_DISPLAY_DATE_MONTH;
						break;
					default:
						formatStr = DateUtil.LINK_DISPLAY_DATE;
				}
				break;
			case DateUtil.DOT_DISPLAY_DATE_SPLIT:
				switch (length) {
					case 14:
						formatStr = DateUtil.DOT_DISPLAY_DATE_FULL;
						break;
					case 6:
						formatStr = DateUtil.DOT_DISPLAY_DATE_MONTH;
						break;
					default:
						formatStr = DateUtil.DOT_DISPLAY_DATE;
				}
				break;
			case DateUtil.CN_DISPLAY_DATE_SPLIT:
				switch (length) {
					case 14:
						formatStr = DateUtil.CN_DISPLAY_DATE_FULL;
						break;
					case 6:
						formatStr = DateUtil.CN_DISPLAY_DATE_MONTH;
						break;
					default:
						formatStr = DateUtil.CN_DISPLAY_DATE;
				}
				break;
			default:
				switch (length) {
					case 14:
						formatStr = DateUtil.DB_STORE_DATE_FULL;
						break;
					case 6:
						formatStr = DateUtil.DB_STORE_DATE_MONTH;
						break;
					default:
						formatStr = DateUtil.DB_STORE_DATE;
				}
				break;
		}
		return formatStr;
	}

	/**
	 * 得到格式化时间串
	 * 
	 * @param date
	 *            指定时间
	 * @param formatStr
	 *            时间格式的类型
	 * @return 指定时间的格式化时间串
	 */
	private static String getDateStr(Date date, String formatStr) {
		SimpleDateFormat fomate = new SimpleDateFormat(formatStr);
		return fomate.format(date);
	}
	
	/**
	 * 将日期格式串(数字字符串)转换为各种显示的格式
	 * 
	 * @param dateStr
	 *            最小6位，最大14位的数据库存储格式时间串如:20041212
	 * @param formatStr
	 *            时间格式的类型
	 * @return 格式化的时间串
	 */
	private static String toDisplayStr(String dateStr, String formatStr) {
		dateStr = dateStr.replaceAll("[^0-9]", "");
		SimpleDateFormat fomateDate = null;
		switch (dateStr.length()) {
			case 4:
				fomateDate = new SimpleDateFormat("yyyy");
				break;
			case 6:
				fomateDate = new SimpleDateFormat("yyyyMM");
				break;
			case 8:
				fomateDate = new SimpleDateFormat("yyyyMMdd");
				break;
			case 10:
				fomateDate = new SimpleDateFormat("yyyyMMddHH");
				break;
			case 12:
				fomateDate = new SimpleDateFormat("yyyyMMddHHmm");
				break;
			case 14:
				fomateDate = new SimpleDateFormat("yyyyMMddHHmmss");
				break;
		}
		try {
			return DateUtil.getDateStr(fomateDate.parse(dateStr), formatStr);
		} catch (ParseException e) {
			return dateStr;
		}
	}
	
	/**
	 * 时间字符格式化检验
	 * @param dateStr
	 * @return
	 * @author:chenxy
	 */
	public static boolean checkDateStr(String dateStr){
		if (dateStr != null) {
			String dateStrCheck = dateStr.replaceAll("[^0-9]", "");
			if(dateStrCheck.length() >= 4 && dateStrCheck.length() <= 14 && dateStrCheck.length() % 2 == 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据格式化类型将时间类型的数据格式化，并以字符串形式返回。
	 * @param object
	 * @param formatStr
	 * @return
	 * @author:chenxy
	 */
	public static String getDateStr(Object object, String formatStr) {
		if (object == null) {
			return null;
		}
		String resultDateStr = null;
		if (object instanceof Date) {
			resultDateStr = getDateStr((Date) object, formatStr);
		} else if (object instanceof String) {
			String dateStr = (String) object;
			if (!checkDateStr(dateStr)) {
				return null;
			}
			resultDateStr = toDisplayStr(dateStr, formatStr);
		} else if (object instanceof Integer) {
			String dateStr = ((Integer) object).toString();
			if (!checkDateStr(dateStr)) {
				return null;
			}
			resultDateStr = toDisplayStr(dateStr, formatStr);
		}
		return resultDateStr;
	}
	
	public static String getCurrentDateStr(String formatStr){
		return getDateStr(new Date(), formatStr);
	}
	
	/**
	 * 根据格式化类型将时间类型的数据格式化，并以字符串形式返回。
	 * @param dateStr
	 * @param formatSliptType
	 * @return
	 * @author:chenxy
	 */
	public static String getDateStrByStr(String dateStr, int formatSliptType) {
		if (!checkDateStr(dateStr)) {
			return dateStr;
		}
		dateStr = dateStr.replaceAll("[^0-9]", "");
		String formateType = getFormatType(formatSliptType, dateStr.length());
		return toDisplayStr(dateStr, formateType);
	}
	
	/**
	 * 转化Date类型
	 * @param dateStr
	 * @return
	 * @author:chenxy
	 */
	public static Date changeStrToDate(String dateStr){
		if (!checkDateStr(dateStr)) {
			return null;
		}
		dateStr = dateStr.replaceAll("[^0-9]", "");
		String formatStr = getFormatType(DateUtil.DB_STORE_DATE_SPLIT, dateStr.length());
		SimpleDateFormat fomateDate = new SimpleDateFormat(formatStr);
		try {
			return fomateDate.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-12-9 下午3:00:07
	 * 日期: 2013-12-9  
	 * 参数: @param dateStr
	 * 参数: @return
	 * 返回: Date
	 * 描述: 得到指定日期的当天开始时间
	 */   
	public static Date changeStrToDayBegin(String dateStr) {
		if (!checkDateStr(dateStr)) {
			return null;
		}
		dateStr = dateStr.replaceAll("[^0-9]", "");
		if (dateStr.length() == 8) {
			dateStr = dateStr + "000000";
		}
		return changeStrToDate(dateStr);
	}
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-12-9 下午3:00:07
	 * 日期: 2013-12-9  
	 * 参数: @param dateStr
	 * 参数: @return
	 * 返回: Date
	 * 描述: 得到指定日期的当天结束时间
	 */   
	public static Date changeStrToDayEnd(String dateStr) {
		if (!checkDateStr(dateStr)) {
			return null;
		}
		dateStr = dateStr.replaceAll("[^0-9]", "");
		if (dateStr.length() == 8) {
			dateStr = dateStr + "235959";
		}
		return changeStrToDate(dateStr);
	}
	
	/**  
	 * 得到指定日期所在月的第一天  
	 * @return  
	 */ 
	public static String getWeekFirstDay(String pointTime, String formatType) {
		Date pointDate = DateUtil.changeStrToDate(pointTime);
		Calendar calendar = Calendar.getInstance(); // 获取日历
		calendar.setTime(pointDate); // 将日历翻到指定日期
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 将日历翻到这周的周一
		// 如果得到的时间大于今天则表示今天是周末，得到的是下周一的时间，因此需要向上翻一周
		if(calendar.getTime().after(pointDate)){
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
		}
		return getDateStr(calendar.getTime(), formatType);
	}

	public static String getWeekLastDay(String pointTime, String formatType) {
		Date pointDate = DateUtil.changeStrToDate(pointTime);
		Calendar calendar = Calendar.getInstance(); // 获取日历
		calendar.setTime(pointDate);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 将日历翻到下周的周天，因为欧洲时间周天是第一天所以本周的周天其实是上周的
		if(calendar.getTime().before(pointDate)){
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		return getDateStr(calendar.getTime(), formatType);
	}
	
	public static String getCurrentWeekFirstDay(String formatType){
		return getWeekFirstDay(DateUtil.getCurrentDateStr(DateUtil.DB_STORE_DATE), formatType);
	}
	
	public static String getCurrentWeekLastDay(String formatType){
		return getWeekLastDay(DateUtil.getCurrentDateStr(DateUtil.DB_STORE_DATE), formatType);
	}
	
	/**  
	 * 得到指定日期所在月的第一天  
	 * @return  
	 */  
	public static String getMonthFirstDay(String pointTime, String formatType) {   
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.changeStrToDate(pointTime));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));   
	    return getDateStr(calendar.getTime(), formatType);
	}
	  
	/**  
	 * 得到指定日期所在月的最后一天  
	 *   
	 * @return  
	 */  
	public static String getMonthLastDay(String pointTime, String formatType) {   
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.changeStrToDate(pointTime));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));   
	    return getDateStr(calendar.getTime(), formatType);
	}
	
	public static String getCurrentMonthFirstDay(String formatType){
		return getMonthFirstDay(DateUtil.getCurrentDateStr(DateUtil.DB_STORE_DATE), formatType);
	}
	
	public static String getCurrentMonthLastDay(String formatType){
		return getMonthLastDay(DateUtil.getCurrentDateStr(DateUtil.DB_STORE_DATE), formatType);
	}

	public static Date addDay(Date origDate, int dayNum) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(origDate);
		instance.add(Calendar.DAY_OF_YEAR, dayNum);
		return instance.getTime();
	}
	
	public static void main(String[] args) {
		Calendar c1 = Calendar.getInstance();
		Date d1 = c1.getTime();
		c1.add(Calendar.DAY_OF_YEAR, 1001);
		Date d2 = c1.getTime();
		System.out.println(getDaysBetween(d1, d2));
		System.out.println(getWeekFirstDay("20101131", DateUtil.DB_STORE_DATE));
		System.out.println(getWeekLastDay("20101131", DateUtil.DB_STORE_DATE));
		System.out.println(getMonthFirstDay("20101131", DateUtil.DB_STORE_DATE));
		System.out.println(getMonthLastDay("20101131", DateUtil.DB_STORE_DATE));
	}
}
