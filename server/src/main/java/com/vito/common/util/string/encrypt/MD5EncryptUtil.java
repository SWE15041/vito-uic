package com.vito.common.util.string.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/7/22 10:12
 */
public class MD5EncryptUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MD5EncryptUtil.class);
    private static final String SALT_KEY = "zhai@ravan";

    /**
     * 获取srcStr的MD5编码（十六进制表示）
     *
     * @param srcStr 需要获取MD5的字符串，不能为null
     * @return srcStr的MD5代码（32个字符）
     */
    public static String encrypt(String srcStr) {
        return getDigest(srcStr, "MD5");
    }

    private static String getDigest(String srcStr, String alg) {
        try {
            MessageDigest alga = MessageDigest.getInstance(alg);
            alga.update(srcStr.getBytes());
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转十六进制字符串
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0");
            }
            hs.append(stmp);
        }
        return hs.toString();
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }

}
