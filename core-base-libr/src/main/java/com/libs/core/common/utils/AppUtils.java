package com.libs.core.common.utils;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.libs.core.R;
import com.libs.core.common.ACache.CommonCacheLoader;
import com.libs.core.common.keys.KeysUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author admin 2019-10-11
 */
public class AppUtils {

    /**
     * dip转pix
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        final float scale = AppContext.getInstance().getContext().getResources().getDisplayMetrics().density;
        //  final float scale1 = AutoSizeConfig.getInstance().getInitDensity();
        //  LogUtil.d("dp2px","scale = " + scale);
        return AutoSizeUtils.pt2px(AppContext.getInstance().getContext(), dpValue);
//        return (int) (dpValue * scale + 0.5f);
    }

    public static String getString(int stringRes) {
        if (AppContext.getInstance().getContext() != null) {
            try {

                return AppContext.getInstance().getContext().getResources().getString(stringRes);
            } catch (Exception e) {
                return AppContext.getInstance().getContext().getResources().getString(R.string.data_error);
            }
        }
        return "";
    }

    public static int getColor(int color) {
        if (AppContext.getInstance() != null) {
            return AppContext.getInstance().getContext().getResources().getColor(color);
        }
        return -1;
    }

    //8.0以上需要特殊判断
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //8.0手机以上
                if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    return false;
                }
            }

            String CHECK_OP_NO_THROW = "checkOpNoThrow";
            String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean overWeek() {
        //用于记录的时间
        long lastShowTime;
        CommonCacheLoader mCommonCacheLoader = CommonCacheLoader.getInstance(AppContext.getInstance().getContext());
        if (mCommonCacheLoader.loadData(KeysUtil.LAST_SHOW_TIME) == null) {
            mCommonCacheLoader.put(KeysUtil.LAST_SHOW_TIME, System.currentTimeMillis());
            return true;
        } else {
            lastShowTime = (long) mCommonCacheLoader.loadData(KeysUtil.LAST_SHOW_TIME);
        }
        //判断上次登陆到现在是否超过一周
        long now = System.currentTimeMillis();
        long data = now - lastShowTime;
        LogUtils.d("AppUtils", "上次登录时间 " + lastShowTime + " 本地时间 " + now + " data= " + data);
//        24 * 60 * 60 * 1000
        if (data > 86400000) {//24小时 86400000
            mCommonCacheLoader.put(KeysUtil.LAST_SHOW_TIME, System.currentTimeMillis());
            return true;
        }
        return false;
    }


    /**
     * 跳到通知栏设置界面
     */
    public static void settingNotification() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, AppContext.getInstance().getContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppContext.getInstance().getContext().startActivity(intent);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", AppContext.getInstance().getContext().getPackageName());
            intent.putExtra("app_uid", AppContext.getInstance().getContext().getApplicationInfo().uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppContext.getInstance().getContext().startActivity(intent);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + AppContext.getInstance().getContext().getPackageName()));
            AppContext.getInstance().getContext().startActivity(intent);
        }
    }
}
