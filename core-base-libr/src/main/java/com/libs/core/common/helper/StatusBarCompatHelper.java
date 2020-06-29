package com.libs.core.common.helper;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

/**
 * @author: amos
 * @date: 2020/1/16 15:42
 * @description: android 状态栏兼容方案
 * 注意: 不能设置fitsystemwindow
 */
public class StatusBarCompatHelper {
    private static final String KEY_STATUS_BAR_HEIGHT = "status_bar_height_cache";
    private static final String TAG = "StatusBarCompat";

    //状态栏字体 白色
    public static void setDarkStatusBar(Activity activity) {
        setStatusBarStyle(activity, false, ContextCompat.getColor(activity, android.R.color.transparent), false);
    }

    //状态栏字体 白色
    public static void setDarkStatusBar(Activity activity, boolean isCover) {
        setStatusBarStyle(activity, false, ContextCompat.getColor(activity, android.R.color.transparent), isCover);
    }

    //状态栏字体 黑色
    public static void setLightStatusBar(Activity activity) {
        setStatusBarStyle(activity, true, ContextCompat.getColor(activity, android.R.color.transparent), false);
    }

    //状态栏字体 黑色
    public static void setLightStatusBar(Activity activity, boolean isCover) {
        setStatusBarStyle(activity, true, ContextCompat.getColor(activity, android.R.color.transparent), isCover);
    }

    /**
     * @param activity
     * @param isDark
     * @param statusBarColor
     * @param isCover        是否在状态栏下芳
     */
    public static void setStatusBarStyle(Activity activity, boolean isDark, @ColorInt int statusBarColor, boolean isCover) {
        //设置mode
        setMode(activity, isDark);
        //设置状态栏高度
        setStatusBarHeight(activity, statusBarColor, isCover);
    }

    //设置状态栏模式
    static void setMode(Activity activity, boolean isDark) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            if (window != null) {
                //window.requestFeature(Window.FEATURE_NO_TITLE);
                int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                if (isDark) {
                    visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                window.getDecorView().setSystemUiVisibility(visibility);
            }
        }
    }

    /**
     * @param activity
     * @param statusBarColor
     * @param isCover        true 布局在状态栏下方  false 反之
     */
    static void setStatusBarHeight(Activity activity, @ColorInt final int statusBarColor, final boolean isCover) {
        final ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        Window window = activity.getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(isCover ? ContextCompat.getColor(activity, android.R.color.transparent) : statusBarColor);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getStatusBarHeight(activity, new OnStatusBarHeightCallBack() {
                @Override
                public void setHeight(int statusBarHeight) {
                    Log.e(TAG, "状态栏高度 : " + statusBarHeight);
                    if (contentView != null) {
                        contentView.setPadding(contentView.getPaddingLeft(), isCover ? 0 : statusBarHeight, contentView.getPaddingRight(), contentView.getPaddingBottom());
                    }
                }
            });
        }
    }


    public static void getStatusBarHeight(Activity activity, OnStatusBarHeightCallBack callBack) {
        int statusBarHeight = SharedCacheHelper.getInstance().getInt(KEY_STATUS_BAR_HEIGHT);
        if (statusBarHeight <= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getStatusBarHeightCompatP(activity, callBack);
                return;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarHeight = getStatusBarHeightNormal(activity);
            }
        }
        if (callBack != null) {
            callBack.setHeight(statusBarHeight);
        }
    }

    static void getStatusBarHeightCompatP(final Activity activity, final OnStatusBarHeightCallBack callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
                View content = activity.findViewById(android.R.id.content);
                content.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                        DisplayCutout displayCutout = insets.getDisplayCutout();
                        if (displayCutout != null) {
                            int displayCutoutHeight = displayCutout.getSafeInsetTop();
                            /*if(displayCutoutHeight <= 0){
                                displayCutoutHeight = getStatusBarHeightNormal(activity);
                            }*/
                            SharedCacheHelper.getInstance().putInt(KEY_STATUS_BAR_HEIGHT, displayCutoutHeight);
                            if (callBack != null) {
                                callBack.setHeight(displayCutoutHeight);
                            }
                        } else {
                            int statusBarHeight = getStatusBarHeightNormal(activity);
                            if (callBack != null) {
                                callBack.setHeight(statusBarHeight);
                            }
                        }
                        return insets;
                    }
                });
            } catch (Exception e) {
                int statusBarHeight = getStatusBarHeightNormal(activity);
                if (callBack != null) {
                    callBack.setHeight(statusBarHeight);
                }
            }

        }
    }

    static int getStatusBarHeightNormal(Activity activity) {
        int statusBarHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
                SharedCacheHelper.getInstance().putInt(KEY_STATUS_BAR_HEIGHT, statusBarHeight);
            }
        }
        return statusBarHeight;
    }

    public interface OnStatusBarHeightCallBack {
        void setHeight(int statusBarHeight);
    }
}
