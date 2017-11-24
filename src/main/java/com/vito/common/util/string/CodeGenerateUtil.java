package com.vito.common.util.string;

import java.util.UUID;
import java.util.zip.CRC32;

public class CodeGenerateUtil {

	private static String ALPHANUMERIC_STR;
	private static String DIGIST_STR;

	static {
		DIGIST_STR = "0123456789";
		String aphaStr = "abcdefghijklmnopqrstuvwxyz";
		ALPHANUMERIC_STR = (new StringBuilder()).append(DIGIST_STR)
				.append(aphaStr).append(aphaStr.toUpperCase()).toString();
	}

	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}

    /**
     * 产生6位英数随机数,区分大小写
     * @return
     */
    public static String generateUpdateKey() {
        return getRandomStr(6);	
    }
    
    /**
     * 产生一个随机英数字符串，区分大小定
     * @param length 随机字符串的长度
     * @return
     */
    public static String getRandomStr(int length) {
		int srcStrLen = ALPHANUMERIC_STR.length();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++) {
			int maxnum = (int)(Math.random()*1000);
			int result = maxnum%srcStrLen;
			char temp = ALPHANUMERIC_STR.charAt(result);
			sb.append(temp);
		}
		return sb.toString();
    }
    
    /**
     * 产生一个随机英数字符串，区分大小定
     * @param length 随机字符串的长度
     * @return
     */
    public static String getRandomDigist(int length) {
        int srcStrLen = DIGIST_STR.length();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++) {
            int maxnum = (int)(Math.random()*1000);
            int result = maxnum%srcStrLen;
            char temp = DIGIST_STR.charAt(result);
            sb.append(temp);
        }
        return sb.toString();
    }
    
	public static long getCRC32(String text) {
		long crc = 0;
		try {
			CRC32 c=new CRC32();
			c.reset();
			c.update(text.getBytes());
			crc=c.getValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return crc;
	}
    
    public static void main(String[] args) {
		System.out.println(generateUUID());
	}
}
