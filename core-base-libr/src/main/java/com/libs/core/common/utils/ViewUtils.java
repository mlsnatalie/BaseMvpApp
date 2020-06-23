package com.libs.core.common.utils;

import android.view.View;

import com.libs.core.common.listener.SetViewAlphaListener;


public class ViewUtils {
    /**
     * @param view
     * 添加点击效果
     */
    public static void setPressEffect(View view){
        view.setOnTouchListener(new SetViewAlphaListener());
    }

    /**
     * @param view
     * 取消点击效果
     */
    public static void cancelPressEffect(View view){
        view.setOnTouchListener(null);
    }

}
