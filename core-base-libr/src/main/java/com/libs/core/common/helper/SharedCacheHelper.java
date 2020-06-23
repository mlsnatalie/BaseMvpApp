package com.libs.core.common.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.libs.core.common.ApplicationProxy;


/**
 * @author: amos
 * @date: 2020/1/17 11:21
 * @description: 使用 SharedPreferences 存储
 * 由于用到 ApplicationProxy 里面的参数 所以在使用的时候一定要初始化 {@link ApplicationProxy}
 */
public class SharedCacheHelper {

    private static SharedCacheHelper mHelper;
    private SharedPreferences mPreferences;
    private static final String FILE_NAME = "common_shared_cache";

    private SharedCacheHelper() {
        mPreferences = ApplicationProxy.getInstance().getApplication().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedCacheHelper getInstance() {
        if (mHelper == null) {
            synchronized (SharedCacheHelper.class) {
                if (mHelper == null) {
                    mHelper = new SharedCacheHelper();
                }
            }
        }
        return mHelper;
    }

    public void putString(String key, String value) {
        mPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key){
        return mPreferences.getString(key,"");
    }

    public void putInt(String key, int value) {
        mPreferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key){
        return mPreferences.getInt(key,0);
    }

    public void putLong(String key, long value) {
        mPreferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key){
        return mPreferences.getLong(key,0L);
    }

    public void putBoolean(String key, Boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public Boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }
}
