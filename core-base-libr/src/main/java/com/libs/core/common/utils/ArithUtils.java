package com.libs.core.common.utils;

import java.math.BigDecimal;

/**
 * 四则算术运算类
 * 1、精确的加减乘除运算
 * 2、精确的四舍五入运算
 *
 * @author zhang.zheng
 * @version 2017-07-06
 */
public class ArithUtils {

    /**
     * 默认运算精度
     */
    private static final int DEFAULT_DIV_SCALE = 10;
    /**
     * 四舍五入模式
     */
    private static final int ROUND_MODE = BigDecimal.ROUND_HALF_EVEN;


    /**
     * 加法
     */
    public static double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2));
    }

    public static double add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }


    /**
     * 减法
     */
    public static double subtract(double v1, double v2) {
        return subtract(Double.toString(v1), Double.toString(v2));
    }

    public static double subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 乘法
     */
    public static double multiply(double v1, double v2) {
        return multiply(Double.toString(v1), Double.toString(v2));
    }

    public static double multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 除法
     */
    public static double divide(double v1, double v2) {
        return divide(Double.toString(v1), Double.toString(v2), DEFAULT_DIV_SCALE);
    }

    public static double divide(String v1, String v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    public static double divide(double v1, double v2, int scale) {
        return divide(Double.toString(v1), Double.toString(v2), scale);
    }

    public static double divide(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, ROUND_MODE).doubleValue();
    }


    /**
     * 四舍五入
     */
    public static double round(double v, int scale) {
        return round(Double.toString(v), scale);
    }

    public static double round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, ROUND_MODE).doubleValue();
    }


}
