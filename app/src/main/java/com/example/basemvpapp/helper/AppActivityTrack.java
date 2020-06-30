package com.example.basemvpapp.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.libs.core.common.utils.ListUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Stack;

/**
 * <pre>
 *     author : amos
 *     time   : 2019/11/11 17:29
 *     desc   : 此类用于 追踪activity 的活动, 判断APP是否已启动, 获取当前活动的activity, 判断应用在前台还是后台
 *     version: 1.0
 * </pre>
 */
public class AppActivityTrack {
    private static AppActivityTrack mTrack;
    private int mActivityCount;
    private WeakReference<Activity> mVisiableActivity;
    private Stack<Activity> mActivityList = new Stack<>();

    private AppActivityTrack() {

    }

    public static AppActivityTrack getInstance() {
        if (mTrack == null) {
            synchronized (AppActivityTrack.class) {
                if (mTrack == null) {
                    mTrack = new AppActivityTrack();
                }
            }
        }
        return mTrack;
    }

    /**
     * 需要在应用的application 中做初始赋值
     *
     * @param application
     */
    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    /**
     * 是否是当前进程
     *
     * @param context
     * @param processId 进程id {@link Process#myPid()}
     * @return
     */
    public boolean isCurrentProcess(Context context, int processId) {
        if (context == null) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
            if (infos != null && infos.size() > 0) {
                for (ActivityManager.RunningAppProcessInfo info : infos) {
                    if (processId == info.pid && info.processName.equals(context.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * app 是否已启动
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAppStartUp(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
            if (infos != null && infos.size() > 0) {
                for (ActivityManager.RunningAppProcessInfo info : infos) {
                    if (info.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 应用是否在前台
     *
     * @return
     */
    public boolean isForeground() {
        if (mActivityCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取顶层的activity
     *
     * @return
     */
    public Activity getTopActivity() {
        if (isForeground()) {
            if (mVisiableActivity != null) {
                return mVisiableActivity.get();
            }
        }
        if (mActivityList != null && mActivityList.size() > 0) {
            return mActivityList.peek();
        }
        return null;
    }

    public boolean isContainCurrentActivity(@NonNull Class<? extends Activity> clazz) {
        if (!ListUtils.isEmpty(mActivityList)) {
            for (Activity act : mActivityList) {
                if (clazz.equals(act.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
            mActivityList.push(activity);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            mActivityCount++;
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            mVisiableActivity = new WeakReference<>(activity);
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            mActivityCount--;
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            mActivityList.remove(activity);
        }
    };
}
