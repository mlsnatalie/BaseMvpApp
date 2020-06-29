package com.libs.core.business.http;

import android.text.TextUtils;

import com.libs.core.business.sensor.SensorsTracker;
import com.libs.core.common.utils.AppContext;
import com.libs.core.common.utils.DeviceInfo;
import com.libs.core.common.utils.DeviceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * HTTP请求公共参数构建类
 *
 * @author zhang.zheng
 * @version 2018-01-08
 */
public class ParamBuilder {

    public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
    public static final MediaType IMAGE = MediaType.parse("image/jpeg");
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 获取请求公共参数
     */
    public static Map<String, String> getBaseParams() {
        Map<String, String> params = new HashMap<>();
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("version", DeviceUtils.getVersionName(AppContext.getInstance().getContext()));// 版本号
//        params.put("device", SensorsTracker.getInstance().getDistinctId());// 设备号
        params.put("appType", "1");// 1-Android 2-IOS
        String channelId = DeviceInfo.getAppStringMetaData(AppContext.getInstance().getContext(), "UMENG_CHANNEL");
        params.put("channelId", TextUtils.isEmpty(channelId) ? "jince" : channelId);
        return params;
    }

    /**
     * 创建文本参数
     *
     * @param param 文本参数
     */
    public static RequestBody createTextBody(String param) {
        return RequestBody.create(TEXT, param);
    }


    /**
     * 创建图片参数
     *
     * @param imgPath 图片路径
     */
    public static RequestBody createImageBody(String imgPath) {
        return RequestBody.create(IMAGE, new File(imgPath));
    }




}
