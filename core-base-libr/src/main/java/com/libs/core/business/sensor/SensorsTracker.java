package com.libs.core.business.sensor;

import android.content.Context;
import android.text.TextUtils;

import com.libs.core.business.http.HttpURL;
import com.libs.core.business.http.vo.CustomUserVo;
import com.libs.core.common.manager.UserManager;
import com.libs.core.common.utils.LogUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 神策统计
 *
 * @author zhang.zheng
 * @version 2018-06-19
 */
public class SensorsTracker {

    // App用户ID
    private String appUid;
    // App注册渠道
    private String appChannel;
    private SensorsDataAPI mSensorsDataAPI;

    private static volatile SensorsTracker instance;

    private SensorsTracker() {

    }

    public synchronized static SensorsTracker getInstance() {
        if (instance == null) {
            synchronized (SensorsTracker.class) {
                if (instance == null) {
                    instance = new SensorsTracker();
                }
            }
        }
        return instance;
    }


    public void init(Context context, boolean debug) {
        // SDK初始化
        SensorsDataAPI.sharedInstance(context,
                HttpURL.getSensorUrl(),
                debug ? SensorsDataAPI.DebugMode.DEBUG_AND_TRACK : SensorsDataAPI.DebugMode.DEBUG_OFF);
        mSensorsDataAPI = SensorsDataAPI.sharedInstance(context);

        // 开启自动追踪
        List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
        mSensorsDataAPI.enableAutoTrack(eventTypeList);

        // 追踪渠道效果
        try {
            JSONObject properties = new JSONObject();
            properties.put("FirstUseTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.CHINA).format(new Date()));
            mSensorsDataAPI.trackInstallation("AppInstall", appendBaseProperties(properties));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d(this, "initSensorsSDK......End");
    }

    /**
     * 神策用户注册
     *
     * @param uid 用户ID
     */
    public void login(String uid) {
        try {
            String distinctId = mSensorsDataAPI.getAnonymousId();
            if (TextUtils.isEmpty(distinctId) || distinctId.contains("-") || !distinctId.matches("\\d+")) {
                LogUtils.d(this, "old distinctId[" + distinctId + "] is AnonymousId, can direct bind");
                // 匿名id并非用户真实id，则直接登录
                mSensorsDataAPI.login(uid);
            } else {
                if (!distinctId.equals(uid)) {
                    LogUtils.d(this, "old distinctId[" + distinctId + "] is uid, should reset");
                    mSensorsDataAPI.resetAnonymousId();
                } else {
                    LogUtils.d(this, "old distinctId[" + distinctId + "] equals new uid, direct login");
                }
                mSensorsDataAPI.login(uid);
            }

            // 保存当前UID
            setAppUid(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 神策用户注销
     */
    public void logout() {
        String distinctId = mSensorsDataAPI.getAnonymousId();
        if (TextUtils.isEmpty(distinctId) || distinctId.contains("-") || !distinctId.matches("\\d+")) {
            LogUtils.d(this, "old distinctId[" + distinctId + "] is AnonymousId, need not reset");
            // 匿名id为设备号，无需重置
        } else {
            // 匿名id为用户id，需要重置
            LogUtils.d(this, "old distinctId[" + distinctId + "] is uid, need reset");
            mSensorsDataAPI.logout();
            mSensorsDataAPI.resetAnonymousId();
        }
    }


    private String getAppUid() {
        // 如果appUid为空，则取客服ID；如果客服ID也为空，则取神策ID
        if (!TextUtils.isEmpty(appUid)) {
            return appUid;
        }
        CustomUserVo customUserVo = UserManager.getInstance().getCustomUserVo();
        if (customUserVo != null && !TextUtils.isEmpty(customUserVo.getUid())) {
            return customUserVo.getUid();
        }
        return mSensorsDataAPI.getAnonymousId();
    }

    public String getAppUid2() {
        // 如果appUid为空，则取客服ID；如果客服ID也为空，则取神策ID
        if (!TextUtils.isEmpty(appUid)) {
            return appUid;
        }
        return mSensorsDataAPI.getAnonymousId();
    }

    private void setAppUid(String appUid) {
        this.appUid = appUid;
        LogUtils.d(this, "sensors set app-uid：" + appUid);
    }


    public String getAppChannel() {
        return "app-android-" + this.appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getDistinctId() {
        return mSensorsDataAPI.getAnonymousId();
    }


    /**
     * 事件追踪1
     * 作用：简单单击事件统计
     *
     * @param eventName 事件名称
     * @param clickName 按钮名称
     */
    public void track(String eventName, String clickName) {
        JSONObject properties = new JSONObject();
        try {
            // 按钮名称
            properties.put(SensorConst.APP_CLICK_NAME, clickName);
            track(eventName, properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 首页小黄条
     * home_littlebar
     *
     * @param eventName
     * @param clickName
     */
    public void trackHomeLittleBar(String eventName, String clickName) {
        JSONObject properties = new JSONObject();
        try {
            // 按钮名称
            properties.put(SensorConst.CLICK_NAME, clickName);
            track(eventName, properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 事件追踪2
     * 作用：入口来源事件统计
     *
     * @param eventName 事件名称
     * @param clickName 按钮名称
     * @param entrance  入口来源
     */
    public void track(String eventName, String clickName, String entrance) {
        JSONObject properties = new JSONObject();
        try {
            // 按钮名称
            properties.put(SensorConst.APP_CLICK_NAME, clickName);
            // 入口来源
            properties.put(SensorConst.CLICK_SRC, entrance);
            track(eventName, properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件追踪3
     * 作用：复杂业务事件统计
     *
     * @param eventName  事件名称
     * @param properties 事件属性
     */
    public void track(String eventName, JSONObject properties) {
        try {
            mSensorsDataAPI.track(eventName, appendBaseProperties(properties));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加公共属性
     */
    private JSONObject appendBaseProperties(JSONObject properties) {
        try {
            // 用户ID
            properties.put(SensorConst.APP_UID, getAppUid());
            // 增加渠道跟踪
            properties.put(SensorConst.APP_CHANNEL, appChannel);
            // 设备类型
            properties.put(SensorConst.APP_PLAT, SensorConst.ANDROID);
            // 创建时间
            properties.put(SensorConst.CREATE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));
            properties.put(SensorConst.APP_SRC, SensorConst.APP_SRC_VALUES);
            // 是否注册（!!!!该统计逻辑有问题，某些情况下无法判断用户是否注册：登录用户一定注册了，但未登录不代表未注册）
//            properties.put("authority", UserManager.getInstance().isLogin() ? "是" : "否");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     *
     *
     * @param isLogin
     */
    public void setProperties(boolean isLogin) {
        try {
            JSONObject object = new JSONObject();
            object.put("login_status", isLogin ? "登录" : "未登录");
            object.put("plat", "Android");
            mSensorsDataAPI.registerSuperProperties(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
