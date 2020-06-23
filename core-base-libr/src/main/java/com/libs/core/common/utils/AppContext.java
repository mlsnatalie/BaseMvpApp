package com.libs.core.common.utils;

import android.app.Application;

/**
 * 全局工具类
 * 使用之前必须在Application中初始化
 */
public class AppContext {

    private Application context;

    private static volatile AppContext instance;


    public synchronized static AppContext getInstance() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    public void init(Application context) {
        this.context = context;
    }


    public Application getContext() {
        return this.context;
    }
}
