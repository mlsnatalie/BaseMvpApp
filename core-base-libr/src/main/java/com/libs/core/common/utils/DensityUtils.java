package com.libs.core.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 尺寸转换工具类
 *
 * @author zhang.zheng
 * @version 2017-03-13
 */
public class DensityUtils {


    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getDisplayMetrics(context));
    }


    public static float px2dp(Context context, float pxVal) {
        final float scale = getDisplayMetrics(context).density;
        return (pxVal / scale);
    }


    public static float px2sp(Context context, float pxVal) {
        return (pxVal / getDisplayMetrics(context).scaledDensity);
    }

    public static int getScrrenWith(Context context){
        return  getDisplayMetrics(context).widthPixels;
    }

    /**
     * init display metrics
     */
    private static synchronized DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return resources.getDisplayMetrics();
    }

}
