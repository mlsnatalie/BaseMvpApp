package com.libs.core.common.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.libs.core.business.consts.PreferConst;

import java.util.Set;

/**
 * SharedPreferences缓存类
 * 使用之前必须在Application类中初始化
 *
 * 见缓存常量{@link PreferConst }
 *
 * @author zhang.zheng
 * @version 2018-05-06
 */
public class PreferenceManager {

    private static final String FILE_NAME = "simple_cache";

    private static SharedPreferences mSharedPreferences;

    private PreferenceManager() {
    }

    public static void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
    }


    public static void remove(String key) {
        mSharedPreferences.edit().remove(key).commit();
    }


    /**
     * int
     */
    public static int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
    }


    /**
     * long
     */
    public static long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public static void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).commit();
    }


    /**
     * float
     */
    public static float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public static void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).commit();
    }


    /**
     * boolean
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }


    /**
     * String
     */
    public static String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public static void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }


    /**
     * Object
     */
    public static Object getObject(String key, Class<?> clazz) {
        String json = mSharedPreferences.getString(key, "");
        if (!TextUtils.isEmpty(json)) {
            try {
                return new Gson().fromJson(json, clazz);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void putObject(String key, Object object) {
        try {
            mSharedPreferences.edit()
                    .putString(key, new Gson().toJson(object))
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Set<String>
     */
    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return mSharedPreferences.getStringSet(key, defValue);
    }

    public static void putStringSet(String key, Set<String> value) {
        mSharedPreferences.edit().putStringSet(key, value).commit();
    }
}
