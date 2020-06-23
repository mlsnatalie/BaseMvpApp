package com.libs.core.common.utils;

import android.graphics.Color;

import java.text.DecimalFormat;

public class QuoteUtils {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    private static int RED_COLOR = Color.parseColor("#FC4242");
    private static int GREEN_COLOR = Color.parseColor("#20B07D");
    private static int NORMAL_COLOR = Color.parseColor("#5a6066");

    public static final int STOCK_COLOR_NORMAL = Color.parseColor("#333333"); //股票默认颜色
    public static final int STOCK_COLOR_UP = Color.parseColor("#E03C34"); //股票涨的颜色
    public static final int STOCK_COLOR_DOWN = Color.parseColor("#1EA373"); //股票跌的颜色

    public static int quoteColor(double value) {
        return value > 0 ? RED_COLOR : value == 0 ? NORMAL_COLOR : GREEN_COLOR;
    }

    public static int selfQuoteColor(double value) {
        return value > 0 ? STOCK_COLOR_UP : value == 0 ? STOCK_COLOR_NORMAL : STOCK_COLOR_DOWN;
    }

    public static String numberFormat2String(String number) {
        return decimalFormat.format(Double.parseDouble(number));
    }

    public static int getIncreaseColor() {
        return RED_COLOR;
    }

    public static int getDecreaseColor() {
        return GREEN_COLOR;
    }

    public static int getNormalColor() {
        return NORMAL_COLOR;
    }
}
