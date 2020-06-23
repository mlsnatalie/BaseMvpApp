package com.libs.core.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.libs.core.R;
import com.libs.core.common.helper.StatusBarCompatHelper;

/**
 * 沉浸式状态栏
 */
public class StatusBarUtils {
    private static final String TAG = StatusBarUtils.class.getSimpleName();

    public static final int COLOR_LIGHT = Color.parseColor("#30000000");


    /**
     * 深色主题状态条
     */
    public static void setDarkStatusBar(Activity activity, View virtualStatusBar, int titleColor, int statusBarColor) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT))
            return;
        // 设置虚拟状态条,如果是图片沉浸，则隐藏虚拟状态条
        if (titleColor > 0) {
            setVirtualStatusBar(activity, virtualStatusBar, titleColor);
            virtualStatusBar.setVisibility(View.VISIBLE);
        } else {
            virtualStatusBar.setVisibility(View.GONE);
        }

        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // API23++
            // 此处状态栏文本为黑色
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // API21-22
            // 此处状态栏文本为白色
            // 5.0状态栏文本是白色，系统不支持修改，此处设置固定的浅色背景衬托
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(COLOR_LIGHT);
        } else {
            // API19-20
            // 暂时缺乏机型适配，未完待续.....
            virtualStatusBar.setVisibility(View.GONE);
        }
    }

    public static void setDarkStatusBar(Activity activity) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT))
            return;
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // API23++
            // 此处状态栏文本为黑色
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // API21-22
            // 此处状态栏文本为白色
            // 5.0状态栏文本是白色，系统不支持修改，此处设置固定的浅色背景衬托
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(COLOR_LIGHT);
        } else {
            // API19-20
            // 暂时缺乏机型适配，未完待续.....
        }
    }

    /**
     * 浅色主题状态条
     */
    public static void setLightStatusBar(Activity activity, View virtualStatusBar, int titleColor) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT))
            return;
        // 设置虚拟状态栏,如果是图片沉浸，则隐藏虚拟状态条
        if (titleColor > 0) {
            setVirtualStatusBar(activity, virtualStatusBar, titleColor);
            virtualStatusBar.setVisibility(View.VISIBLE);
        } else {
            virtualStatusBar.setVisibility(View.GONE);
        }

        // 浅色主题，状态栏全部为透明，文本显示为白色
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // API23++
            // 此处状态栏文本为白色
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // API21-22
            // 此处状态栏文本为白色
            // 5.0状态栏文本是白色，系统不支持修改，此处设置透明背景
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            // API19-20
            // 暂时缺乏机型适配，未完待续.....
            virtualStatusBar.setVisibility(View.GONE);
        }
    }

    public static void setLightStatusBar(Activity activity) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT))
            return;
        // 浅色主题，状态栏全部为透明，文本显示为白色
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // API23++
            // 此处状态栏文本为白色
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // API21-22
            // 此处状态栏文本为白色
            // 5.0状态栏文本是白色，系统不支持修改，此处设置透明背景
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            // API19-20
            // 暂时缺乏机型适配，未完待续.....

        }
    }

    public static void setFullScreen(Activity activity) {

        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(option);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }
    }

    /**
     * 设置虚拟状态条
     */
    private static void setVirtualStatusBar(Activity activity, View statusBar, int titleColor) {
        StatusBarCompatHelper.getStatusBarHeight(activity, new StatusBarCompatHelper.OnStatusBarHeightCallBack() {
            @Override
            public void setHeight(int statusBarHeight) {
                ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = statusBarHeight;
                statusBar.setLayoutParams(layoutParams);
                statusBar.setBackgroundResource(titleColor);
            }
        });
      /*  ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getStatusBarHeight(activity);
        statusBar.setLayoutParams(layoutParams);
        statusBar.setBackgroundResource(titleColor);*/
    }


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        LogUtils.d(TAG, "状态条高度：" + result);
        return result;
    }

    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

}

