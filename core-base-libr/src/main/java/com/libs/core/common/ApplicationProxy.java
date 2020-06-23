package com.libs.core.common;

import android.app.Application;

/**
 * @author: amos
 * @date: 2020/1/17 10:59
 * @description: 用于存储一些配置信息
 */
public class ApplicationProxy implements ApplicationProxyContract {

    private static volatile ApplicationProxy mProxy;
    private Application mApplication;

    private ApplicationProxy() {

    }

    public static ApplicationProxy getInstance() {
        if (mProxy == null) {
            synchronized (ApplicationProxy.class) {
                if (mProxy == null) {
                    mProxy = new ApplicationProxy();
                }
            }
        }
        return mProxy;
    }


    @Override
    public ApplicationProxy setApplication(Application application) {
        mApplication = application;
        return this;
    }

    @Override
    public Application getApplication() {
        return mApplication;
    }
}
