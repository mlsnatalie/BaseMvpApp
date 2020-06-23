package com.libs.core.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.libs.core.BuildConfig;
import com.libs.core.common.base.tracker.ActivityTracker;
import com.libs.core.common.manager.PreferenceManager;
import com.libs.core.common.utils.LogUtils;
import com.yhao.floatwindow.FloatWindow;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.unit.Subunits;
import me.jessyan.autosize.utils.ScreenUtils;

import static android.support.constraint.Constraints.TAG;

/**
 * Application基类
 *
 * @author zhang.zheng
 * @version 2017-05-08
 */
public abstract class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configUnits();
        ActivityTracker.getInstance().register(this);
        PreferenceManager.init(this);
        getResources();
        init();
        // 初始化浮动框生命周期管理
        FloatWindow.initLifecycle(this);
    }

    /**
     * 具体业务初始化
     */
    protected abstract void init();

    /**
     * 重写系统getResources接口，防止修改系统字体大小导致应用布局错乱。
     * 设置应用全局字体的默认配置，避免受系统字体大小设置影响导致界面错乱。
     */
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null && res.getConfiguration().fontScale != 1) {//非默认值
            //LogUtils.d(TAG, "getResources");
            Configuration config = res.getConfiguration();
            res.getConfiguration().fontScale = 1;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return res;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化屏幕适配
     */
    private void configUnits() {
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {

                        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[1]);
                            AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[0]);
                        } else {
                            AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                            AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        }
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {

                    }
                })
                .setLog(BuildConfig.DEBUG)
                .setCustomFragment(true)
                .getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.PT);
    }

}
