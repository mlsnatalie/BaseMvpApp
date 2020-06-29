package com.libs.core.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Intent工具类
 *
 * @author zhang.zheng
 * @version 2018-05-13
 */
public class IntentUtils {

    private static final String FILE_PROVIDER_NAME = "fileprovider";


    /**
     * 拨打电话
     *
     * @param tel 电话号码
     */
    public static Intent buildCallIntent(String tel) {
        return new Intent("android.intent.action.CALL", Uri.parse("tel:" + tel));
    }


    /**
     * 发送短信
     *
     * @param mobile  手机号码
     * @param message 消息内容
     */
    public static Intent buildSmsIntent(String mobile, String message) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address", mobile);
        intent.putExtra("sms_body", message);
        return intent;
    }

    /**
     * 打开系统相机
     */
    public static void startCameraAction(@NonNull Activity activity, File file, int requestCode) {
        startCameraAction(activity, getUriForFile(activity, file), requestCode);
    }

    /**
     * 打开系统相机
     */
    public static void startCameraAction(@NonNull Activity activity, Uri uri, int requestCode) {
        PackageManager pm = activity.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestCode);
        }
    }


    /**
     * 打开系统相册
     */
    public static void startPhotoAction(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 打开系统设置
     */
    public static void openSystemSettings(@NonNull Context context) {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    /**
     * 打开安装APK文件
     */
    public static void openInstallApk(@NonNull Context context, @NonNull File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String fileType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getMIMEType(file));
        } else {
            fileType = "application/vnd.android.package-archive";
        }
        intent.setDataAndType(getUriForFile(context, file), fileType);
        context.startActivity(intent);
    }

    /**
     * 获取文件URI
     */
    public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + "." + FILE_PROVIDER_NAME, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 获取文件类型
     */
    private static String getMIMEType(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


}
