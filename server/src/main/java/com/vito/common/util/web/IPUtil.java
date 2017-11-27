package com.vito.common.util.web;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class IPUtil {
	
	/**
	 * 获取域名
	 * 
	 * @param url
	 * @return
	 */
	public static String getDomain(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		String domain = null;
		try {
			if (!(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
				url = "http://" + url;
			}
			domain = new URL(url).getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return domain;
	}
	
	/**
	 * 获取URL对应的IP地址
	 * 
	 * @param url
	 * @return
	 */
	public static String getUrlIp(String url) {
		String ip = null;
		try {
			InetAddress address = InetAddress.getByName(getDomain(url));
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	//判断IP是否在指定范围内  
	public static boolean isIpInScope(String ipScope, String ip) {
		if (ipScope == null) {
			throw new NullPointerException("IP段不能为空！");
		}
		if (ip == null) {
			throw new NullPointerException("IP不能为空！");
		}
		ipScope = ipScope.trim();
		ip = ip.trim();
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		if (!ipScope.matches(REGX_IPB) || !ip.matches(REGX_IP)) {
			return false;
		}
		int idx = ipScope.indexOf('-');
		String[] sips = ipScope.substring(0, idx).split("\\.");
		String[] sipe = ipScope.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return ips <= ipt && ipt <= ipe;
	}
	
	public static void main(String[] args) {
		System.out.println(isIpInScope("192.168.5.1-192.168.5.10", "192.168.5.11"));
	}
}
