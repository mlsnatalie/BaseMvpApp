package com.libs.core.business.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: amos
 * @date: 2020/6/12 13:32
 * @description: 通用参数代理
 */
public class HttpCommonParamsProxy {

    private static HttpCommonParamsProxy proxy;

    private HttpCommonParamsProxy() {

    }

    public static HttpCommonParamsProxy getInstance() {
        if (proxy == null) {
            synchronized (HttpCommonParamsProxy.class) {
                if (proxy == null) {
                    proxy = new HttpCommonParamsProxy();
                }
            }
        }
        return proxy;
    }

    private boolean isDebug;
    private String appVersion; //app 版本号
    private String appChannel; //应用渠道
    private String appAndroidId; //android id
    private String appOAId; //
    private String appImei; //


    public HttpCommonParamsProxy setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public HttpCommonParamsProxy setAppChannel(String appChannel) {
        this.appChannel = appChannel;
        return this;
    }

    public HttpCommonParamsProxy setAppAndroidId(String appAndroidId) {
        this.appAndroidId = appAndroidId;
        return this;
    }

    public HttpCommonParamsProxy setAppImei(String appImei) {
        this.appImei = appImei;
        return this;
    }

    public HttpCommonParamsProxy setAppOAId(String appOAId) {
        this.appOAId = appOAId;
        return this;
    }

    public HttpCommonParamsProxy setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public String getAppAndroidId() {
        return appAndroidId;
    }

    public String getAppOAId() {
        return appOAId;
    }

    public String getAppImei() {
        return appImei;
    }


}
