package com.libs.core.common.utils;

import android.content.Context;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.io.File;

public class MyUtils {

    private static MyUtils utils;

    private MyUtils() {

    }

    public static MyUtils getInstance() {
        if (utils == null)
            utils = new MyUtils();
        return utils;
    }

    /**
     * 获取缓存目录中的自定义目录
     *
     * @param context 上下文
     * @param path    自定义目录,如果外设磁盘未挂载成功则使用内部缓存目录，这个参数才有效
     * @return File
     */
    public File getFile(Context context, String path) {

        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // In some case, even the sd card is mounted,
            // getExternalCacheDir will return null
            // may be it is nearly full.
            StringBuilder sb = new StringBuilder();
            File dir = context.getExternalFilesDir(null);//获取的是根 data/data/files/
            if (dir != null) {
                sb.append(dir.getAbsolutePath()).append(File.separator).append(path).append(File.separator);
            } else {
                sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                        .append("/files/").append(path).append(File.separator).toString();
            }

//			file = new File(context.getExternalCacheDir().getPath() + "/"
//					+ path);
            file = new File(sb.toString());
        } else {
            //如果外部sdcard没有挂载，则使用内部文件目录
            file = new File(context.getFilesDir() + "/" + path);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    /**
     * 获取缓存目录中的自定义目录
     *
     * @param context 上下文
     * @param path    自定义目录,如果外设磁盘未挂载成功则使用内部缓存目录，这个参数才有效
     * @return File
     */
    public File getCache(Context context, String path) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // In some case, even the sd card is mounted,
            // getExternalCacheDir will return null
            // may be it is nearly full.
            StringBuilder sb = new StringBuilder();
            File dir = context.getExternalCacheDir();
            if (dir != null) {
                sb.append(dir.getAbsolutePath()).append(File.separator).append(path).append(File.separator);
            } else {
                sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                        .append("/cache/").append(path).append(File.separator).toString();
            }

//			file = new File(context.getExternalCacheDir().getPath() + "/"
//					+ path);
            file = new File(sb.toString());
        } else {
            file = new File(context.getCacheDir() + "/" + path);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 设置一串数组的颜色
     *
     * @param strs
     * @param colors
     * @return SpannableString
     */
    public SpannableString formatText(String[] strs, int[] colors) {
        if (strs == null || colors == null) {
            return new SpannableString("");
        } else if (strs.length != strs.length)
            return new SpannableString("");

        String str = "";
        for (int i = 0; i < strs.length; i++) {
            str += strs[i];
        }
        SpannableString spa = new SpannableString(str);
        String str1 = "";
        for (int i = 0; i < strs.length; i++) {
            spa.setSpan(new ForegroundColorSpan(colors[i]), str1.length(),
                    str1.length() + strs[i].length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str1 += strs[i];
        }
        return spa;
    }


}
