package com.libs.core.common.permission;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.libs.core.BuildConfig;
import com.libs.core.R;
import com.libs.core.common.utils.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * RxPermissions 方式进行权限请求和判断。
 * 经过测试如果一个权限授权，重复请求权限会返回false.
 *
 * @author bond.zou on 2018/9/12.
 */
public class PermissionHelper<T extends FragmentActivity> {
    private static final String TAG = PermissionHelper.class.getSimpleName();
    /* 请求设置允许安装未知来源应用*/
    public static final int PERMISSION_REQUEST_CODE_SET_INSTALL_PKG = 0xffee;
    private RxPermissions rxPermissions;
    private boolean isRequesting;

    private T activity;

    IPermissionCallback callback;

    public static String[] ALL_PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    public static String[] CALL_PHONE = new String[]{
            Manifest.permission.CALL_PHONE
    };

    public static String[] PHOTO_PERMISSION = new String[]{
            Manifest.permission.CAMERA
    };
    public static String[] RECORD_AUDIO_PERMISSION = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * 禁止不再提示
     */
    public static String FORBIDDEN_NEVER_ASK = "Forbidden Never ask again";

    public PermissionHelper(T activity) {
        this.activity = activity;
        rxPermissions = new RxPermissions(activity);
        rxPermissions.setLogging(BuildConfig.DEBUG);
    }

    /**
     * 判断输入的权限列表是否已授权。
     *
     * @param permissions
     * @return 返回未授权的列表
     */
    private List<String> getPermissionDeniedList(String... permissions) {
        List<String> permissionDeniedList = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionDeniedList.add(permission);
            }
        }

        return permissionDeniedList;
    }

    /**
     * 显示缺失权限提示。
     * 提示跳转设置中心，若不跳转关闭应用。
     */
    public void showMissingPermissionDialog(@NonNull final String permission) {
        int permissionTipsId = R.string.permission_string_help_text;
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                permissionTipsId = R.string.permission_string_sdcard_help_text;
                break;
            case Manifest.permission.CALL_PHONE:
                permissionTipsId = R.string.permission_string_phone_help_text;
                break;
            case Manifest.permission.CAMERA:
                permissionTipsId = R.string.permission_string_camera_help_text;
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                permissionTipsId = R.string.permission_string_coarse_location_help_text;
                break;
            case Manifest.permission.REQUEST_INSTALL_PACKAGES:
                permissionTipsId = R.string.permission_string_install_pkg;
                break;
        }
        String appName = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
        final String finalPermissionTips = activity.getString(permissionTipsId).replaceAll("%s", appName);
        new AlertDialog.Builder(activity, R.style.TransparentDialog)
                .setTitle(R.string.permission_title)
                .setMessage(finalPermissionTips)
                // 拒绝
                .setNegativeButton(R.string.permission_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isRequesting = false;
                        dialog.dismiss();
                        if (callback != null)
                            callback.onPermissionsResult(false, permission, FORBIDDEN_NEVER_ASK);
                    }
                })
                .setPositiveButton(R.string.permissions_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isRequesting = false;
                        if (finalPermissionTips.equals(activity.getString(R.string.permission_string_install_pkg))) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                            intent.setData(Uri.parse("package:" + activity.getApplication().getPackageName()));
                            activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE_SET_INSTALL_PKG);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + activity.getPackageName()));
                            activity.startActivity(intent);
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void requestPermissions(final String... permissions) {
        rxPermissions.requestEachCombined(permissions)//只接收一个结果，三种状态：全授权、至少一个拒绝、至少一个拒绝且不再提示
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            // 用户已经同意所有权限
                            LogUtils.d(TAG, "all permission is granted.");
                            setRequesting(false);
                            if (callback != null)
                                callback.onPermissionsResult(true, permission.name);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            LogUtils.d(TAG, permission.name + " is denied. More info should be provided.");
                            // 如果拒绝授权且未设置不再询问，那就重复请求权限
                            // showMissingPermissionDialog(permission.name);
                            List<String> nograntedList = getPermissionDeniedList(permissions);
                            requestPermissions(nograntedList.toArray(new String[nograntedList.size()]));
                            if (callback != null)
                                callback.onPermissionsResult(false, permission.name);
                        } else {
                            LogUtils.d(TAG, permission.name + " is denied. More info should be provided.");
                            // 用户拒绝了该权限，选中『不再询问』（Never ask again）,那么下次再次启动时，不会提示请求权限的对话框
                            // 对话框提示，缺少权限，如需再申请，需要跳转设置中心
                            showMissingPermissionDialog(permission.name);
                            if (callback != null)
                                callback.onPermissionsResult(false, permission.name);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .as(AutoDispose.<Permission>autoDisposable(AndroidLifecycleScopeProvider.from(activity, Lifecycle.Event.ON_DESTROY)))
//                .subscribe(new Consumer<Permission>() {
//                    @Override
//                    public void accept(Permission permission) throws Exception {
//                        if (permission.granted) {
//                            // 用户已经同意所有权限
//                            LogUtils.d(TAG, "all permission is granted.");
//                            if (activity instanceof onRequestPermissionsResult2)
//                                ((onRequestPermissionsResult2) activity).onGranted(permission);
//                        } else if (permission.shouldShowRequestPermissionRationale) {
//                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            LogUtils.d(TAG, permission.name + " is denied. More info should be provided.");
//                            // 如果拒绝授权且未设置不再询问，那就重复请求权限
//                            // showMissingPermissionDialog(permission.name);
//                            List<String> nograntedList = getPermissionDeniedList(permissions);
//                            requestPermissions(nograntedList.toArray(new String[nograntedList.size()]));
//                            if (activity instanceof onRequestPermissionsResult2)
//                                ((onRequestPermissionsResult2) activity).onDenied(permission, true);
//                        } else {
//                            LogUtils.d(TAG, permission.name + " is denied. More info should be provided.");
//                            // 用户拒绝了该权限，选中『不再询问』（Never ask again）,那么下次再次启动时，不会提示请求权限的对话框
//                            // 对话框提示，缺少权限，如需再申请，需要跳转设置中心
//                            showMissingPermissionDialog(permission.name);
//                            if (activity instanceof onRequestPermissionsResult2)
//                                ((onRequestPermissionsResult2) activity).onDenied(permission, false);
//                        }
//                    }
//                });
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public PermissionHelper setRequesting(boolean requesting) {
        isRequesting = requesting;
        return this;
    }

    public boolean checkPermission(String[] permission, IPermissionCallback callback) {
        this.callback = callback;
        if (!isRequesting()) {
            LogUtils.d(TAG, "checkPermision:" + permission.toString());
            // 此处及其重要，必须设置是否正在请求的状态，因为RxPermissions打开授权会循环执行onResume
            setRequesting(true);
            List<String> permissionDeniedList = getPermissionDeniedList(permission);
            if (permissionDeniedList.isEmpty()) {
                LogUtils.d(TAG, "is Granted");
                setRequesting(false);
                return true;
            } else {
                LogUtils.d(TAG, "no Granted");
                requestPermissions(permissionDeniedList.toArray(new String[permissionDeniedList.size()]));
                return false;
            }
        }
        return false;
    }
}
