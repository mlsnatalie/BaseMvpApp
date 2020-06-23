package com.libs.core.common.utils;

/**
 * @author: amos
 * @date: 2020/3/6 12:49
 * @description:
 */
public class NumberFormatUtils {

    private NumberFormatUtils() {

    }

    public static String format(long number) {
        if (number <= 0) {
            return "";
        }
        float tempCount = 0;
        if (number < 10000) {
            return number + "";
        } else if (number < 100000000) { //以万为单位
            tempCount = number / 10000f;
            return String.format("%.1f", tempCount) + "万";
        }
        tempCount = number / 100000000f;
        return String.format("%.1f", tempCount) + "亿";
    }

    public static String format2(long number) {
        if (number <= 0) {
            return "";
        }
        float tempCount = 0;
        if (number < 10000) {
            return number + "";
        } else if (number < 100000000) { //以万为单位
            tempCount = number / 10000f;
            return String.format("%.1f", tempCount) + "w";
        }
        tempCount = number / 100000000f;
        return String.format("%.1f", tempCount) + "亿";
    }
}
