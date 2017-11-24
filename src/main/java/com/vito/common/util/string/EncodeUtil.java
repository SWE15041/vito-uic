package com.vito.common.util.string;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodeUtil {
	
	private static final Logger LOG = Logger.getLogger(EncodeUtil.class);
	
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String GBK = "GBK";
	public static final String UTF_8 = "UTF-8";
	
	/**
	 * 转换字符串的编码方式
	 * 
	 * @param sourceStr
	 * @param sourceEncode
	 * @param destEncode
	 * @return
	 */
	public static String convertStrEncode(String sourceStr, String sourceEncode, String destEncode){
		String destStr = "";
		try {
			destStr = new String(sourceStr.getBytes(sourceEncode), destEncode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return destStr;
	}
	
	public static String decodeStr(String sourceStr, String charset) {
		try {
			String destStr = URLDecoder.decode(sourceStr, charset);
			return destStr;
		} catch (Exception e) {
			LOG.error("字符串编码转义出错", e);
			throw new RuntimeException("字符串编码转义出错", e);
		}
	}
	
	public static String encodeStr(String sourceStr, String charset) {
		try {
			String destStr = URLEncoder.encode(sourceStr, charset);
			return destStr;
		} catch (Exception e) {
			LOG.error("字符串编码转义出错", e);
			throw new RuntimeException("字符串编码转义出错", e);
		}
	}
	
	public static void main(String[] args) {
		String url = "http://search.paipai.com/cgi-bin/comm_search1?sClassid=0&shoptype=&searchType=0&charSet=gbk&KeyWord=";
		System.out.println(url+encodeStr("测试", GBK));
		String encodeUrl = encodeStr("/photo/#/raw", EncodeUtil.UTF_8);
		System.out.println(encodeUrl);
		System.out.println(encodeStr(encodeUrl, EncodeUtil.UTF_8));
		System.out.println(decodeStr("%D4%A4%B8%E6+%C6%AC%BB%A8", GBK));
		System.out.println(decodeStr("%C6%FA%D3%A4%B5%BA", GBK));
	}

}
