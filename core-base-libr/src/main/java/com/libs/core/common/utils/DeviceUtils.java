package com.libs.core.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.libs.core.common.manager.PreferenceManager;

import java.util.UUID;

/**
 * 设备信息工具类
 *
 * @author zhang.zheng
 * @version 2017-09-25
 */
public class DeviceUtils {

    // 设备唯一标识
    private static final String DEVICE_ID = "device_id";

    /**
     * App版本名
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取手机唯一标识
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceIMEI(Context context) {
        String serial = null;
        if (context!=null && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        serial = !TextUtils.isEmpty(tm.getImei()) ? tm.getImei() : tm.getMeid();
                    } else {
                        serial = !TextUtils.isEmpty(tm.getDeviceId()) ? tm.getDeviceId() : Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 生产一个随机数并永久保存
        if (TextUtils.isEmpty(serial)) {
            String deviceId = PreferenceManager.getString(DEVICE_ID, "");
            if (!TextUtils.isEmpty(deviceId)) {
                serial = deviceId;
            } else {
                serial = UUID.randomUUID().toString().replace("-", "");
                PreferenceManager.putString(DEVICE_ID, serial);
            }
        }
        Log.d("DeviceUtils", "设备序列号：" + serial);
        return serial;
    }

    /**
     * 获取手机UUID
     */
    @SuppressLint("HardwareIds")
    public static String getUUID() {
        String uuid = null;
        uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    /**
     * 获取手机IMEI
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        String IMEI = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                if (TextUtils.isEmpty(tm.getDeviceId())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        IMEI = tm.getImei();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        IMEI = tm.getDeviceId(0);
                    }
                } else {
                    IMEI = tm.getDeviceId();
                }
                Log.d("DeviceUtils", "IMEI：" + IMEI);
                return IMEI;
            }
        }
        return "";
    }

    /**
     * 获取手机Android Id
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                String androidId = Settings.Secure.getString(
                        context.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("DeviceUtils", "androidId：" + androidId);
                return androidId;
            }
        }
        return "";
    }

    /**
     * 获取手机制造商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机制造商、型号等信息
     *
     * @return
     */
    public static String getMobileInfo() {
        return getManufacturer() + "/" + getBrand() + "/" + getModel();
    }

    /**
     * 获取应用程序名称
     */
    public static  String getAppName(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return info.loadLabel(context.getPackageManager()).toString();
            /*PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
