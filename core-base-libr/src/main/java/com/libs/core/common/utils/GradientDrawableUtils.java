package com.libs.core.common.utils;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorInt;

/**
 * @author: amos
 * @date: 2020/3/6 13:34
 * @description:
 */
public class GradientDrawableUtils {

    public static GradientDrawable createGradientDrawable(@ColorInt int startColor, @ColorInt int endColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setColors(new int[]{startColor, endColor});
        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return gradientDrawable;
    }

    public static GradientDrawable createGradientDrawable(@ColorInt int startColor, @ColorInt int endColor, GradientDrawable.Orientation orientation) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setColors(new int[]{startColor, endColor});
        gradientDrawable.setOrientation(orientation);
        return gradientDrawable;
    }

    public static GradientDrawable createGradientRadiusDrawable(@ColorInt int startColor, @ColorInt int endColor, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setColors(new int[]{startColor, endColor});
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        // top-left, top-right, bottom-right, bottom-left
        gradientDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        return gradientDrawable;
    }

    public static GradientDrawable createRadiusBgDrawable(@ColorInt int bgColorInt, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(bgColorInt);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }



}
