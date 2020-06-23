package com.libs.core.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * 进程相关的工具类
 */
public class ProcessUtils {

    private static final String TAG = ProcessUtils.class.getSimpleName();

    /**
     * 判断某个服务是否正在运行
     *
     * @param context     上下文
     * @param serviceName 服务名（com.demo.service.TestService）
     * @return boolean
     */
    public static boolean isServiceWork(Context context, String serviceName) {
        boolean isWork = false;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                List<ActivityManager.RunningServiceInfo> myList = activityManager.getRunningServices(40);
                if (myList.size() <= 0) {
                    return false;
                }
                for (int i = 0; i < myList.size(); i++) {
                    String mName = myList.get(i).service.getClassName();
                    if (mName.equals(serviceName)) {
                        isWork = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isWork;
    }

    /**
     * 获取应用当前进程
     */
    public static String getCurrProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                    if (appProcess.pid == pid) {
                        return appProcess.processName;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                LogUtils.i("==", "此appimportace =" + appProcess.importance
                        + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    LogUtils.i("==", "处于后台" + appProcess.processName);
                    return true;
                } else {
                    LogUtils.i("==", "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
