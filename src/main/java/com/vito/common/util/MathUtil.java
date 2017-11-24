package com.vito.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MathUtil {

    /**
     * 两个数相加为a+b;
     *
     * @param a
     * @param b
     * @return
     */
    public static double addBigDecimal(double a, double b) {
        BigDecimal ba = BigDecimal.valueOf(a);
        BigDecimal bb = BigDecimal.valueOf(b);
        BigDecimal bc = ba.add(bb);
        return bc.doubleValue();
    }

    /**
     * 两个数相减为a-b;
     *
     * @param a
     * @param b
     * @return
     */
    public static double reduceBigDecimal(double a, double b) {
        BigDecimal ba = BigDecimal.valueOf(a);
        BigDecimal bb = BigDecimal.valueOf(b);
        BigDecimal bc = ba.subtract(bb);
        return bc.doubleValue();
    }

    /**
     * 两个数相乘 a*b=c；
     *
     * @param a
     * @param b
     * @return
     */
    public static double multiplyDouble(double a, double b) {
        BigDecimal ba = BigDecimal.valueOf(a);
        BigDecimal bb = BigDecimal.valueOf(b);
        BigDecimal bc = ba.multiply(bb);
        return bc.doubleValue();
    }

    /**
     * 两个数相除 a/b=c；
     *
     * @param a
     * @param b
     * @return
     */
    public static double divideDouble(double a, double b) {
        BigDecimal ba = BigDecimal.valueOf(a);
        BigDecimal bb = BigDecimal.valueOf(b);
        BigDecimal bc = ba.divide(bb, ba.scale(), BigDecimal.ROUND_HALF_UP);
        return bc.doubleValue();
    }

    /**
     * 两个数相除 a/b=c；
     *
     * @param a
     * @param b
     * @param scale 取值精度
     * @return
     */
    public static double divideDouble(double a, double b, int scale) {
        BigDecimal ba = BigDecimal.valueOf(a);
        BigDecimal bb = BigDecimal.valueOf(b);
        BigDecimal bc = ba.divide(bb, scale, BigDecimal.ROUND_HALF_UP);
        return bc.doubleValue();
    }

    /**
     * 比较两个double大小
     *
     * @param a
     * @param b
     * @return
     */
    public static int compareDouble(double a, double b) {
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        return data1.compareTo(data2);
    }

    /**
     * 分隔任意输入数字的整数与小数部分
     *
     * @param a
     * @return
     */
    public static String breakFloat(double a, double b) {
        double c = a / b;
        DecimalFormat d = new DecimalFormat("#.##");
        String result = "";
        double data = Double.parseDouble(d.format(c));
        int Int = (int) data;
        double digitls = data - Int;
        result = Int + " " + d.format(digitls);
        return result;
    }

    /**
     * 作者: zhongx
     * 版本: 2014-12-24 上午11:44:22
     * 日期: 2014-12-24
     * 参数: @param rate 目标值
     * 参数: @param n 保留小数位数
     * 参数: @return
     * 返回: String
     * 描述:
     */
    public static String percent(double rate, int n) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(n);//设置保留小数位
        nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
        String percent = nf.format(rate);
        return percent;
    }

}
