package com.libs.core.common.utils;

import android.text.TextUtils;

public class MinProgramUtils {

    public static MinProgramUtils instance;

    private String name;

    private String path;

    private MinProgramUtils() {

    }

    public static MinProgramUtils getInstance() {
        if (instance == null) {
            synchronized (MinProgramUtils.class) {
                if (instance == null) {
                    instance = new MinProgramUtils();
                }
            }
        }

        return instance;
    }

    public boolean isEnable() {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
