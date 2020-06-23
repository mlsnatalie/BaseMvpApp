package com.libs.core.common.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libs.core.common.base.BaseRxActivity;

/**
 * @author zhangxiaowei 2019-10-15
 */
public class ImageLoadUtil {

    public static void show(String url, ImageView view) {
        Glide.with(AppContext.getInstance().getContext()).load(url).into(view);
    }

    public static void show(BaseRxActivity activity, String url, ImageView view) {
        Glide.with(activity).load(url).into(view);
    }
}
