package com.libs.core.common.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;


/**
 * 
 * 访问手机内存和外部存储器存储容量的工具类
 * 主要用来获取内存和外部存储器的存储空间使用大小
 * 
 * @author  ZhangZheng
 * @version  2014-08-25 16:00
 */
public class MemoryUtil {
    
    public static final int ERROR = -1;// 外部存储器不可用
    
    /**
     * 外部存储是否可用
     * @return boolean
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    /**
     * 获取手机内存空间大小
     * @return 内存大小
     */
    @SuppressWarnings("deprecation")
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();//获取到Android中的data数据目录
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }
    
    /**
     * 获取手机内存可用空间大小
     * @return 可用内存大小
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();//获取到Android中的data数据目录
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }
    
    /**
     * 获取手机外置SD卡空间大小
     * 注意：部分手机还有内置的SD卡
     * @return 外部存储器大小
     */
    @SuppressWarnings("deprecation")
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }
    
    /**
     * 获取手机外置SD卡可用空间大小
     * @return 外部存储器可用大小
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }
    
    /**
     * 检查外部SD卡的可用空间是否能够存储某个文件
     */
    public static boolean checkSDStorageAvailable(long fileLength) {
        return getAvailableExternalMemorySize() >= fileLength;
    }
    
    /**
     * 转换long类型的字节数为指定字符串
     * 
     * @param size 字节大小
     * @return 1000MiB
     */
    public static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KiB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MiB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
}
