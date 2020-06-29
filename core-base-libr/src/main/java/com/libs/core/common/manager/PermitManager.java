package com.libs.core.common.manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.libs.core.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限授予管理类
 *
 * @author zhang.zheng
 * @version 2018-05-22
 */
public class PermitManager {

    // 应用所需权限
    private final String[] APP_ALL_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.READ_PHONE_STATE
    };
    // 授权请求码
    public static final int REQUEST_CODE_GRANT_PERMIT = 0x1111;
    // 未授权限
    private List<String> mNotGrantPermissions = new ArrayList<>();

    private Activity mActivity;


    public PermitManager(Activity activity) {
        mActivity = activity;
        findNotGrantPermissions();
    }


    /**
     * 找出未授权的权限
     */
    private void findNotGrantPermissions() {
        mNotGrantPermissions.clear();
        for (String permission : APP_ALL_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                mNotGrantPermissions.add(permission);
            }
        }

        LogUtils.d(this, "未授权限个数：" + mNotGrantPermissions.size());
    }


    /**
     * 获取未授权限类别
     */
    public String[] getNotGrantPermissions() {
        findNotGrantPermissions();
        return mNotGrantPermissions.size() > 0 ? mNotGrantPermissions.toArray(new String[mNotGrantPermissions.size()]) : null;
    }


    /**
     * 检查是否有未授权限
     */
    public boolean hasNotGrantPermissions() {
        return !mNotGrantPermissions.isEmpty();
    }


    /**
     * 请求权限
     */
    public void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(mActivity, permissions, REQUEST_CODE_GRANT_PERMIT);
    }


    /**
     * 授权结果回调
     */
    public boolean onRequestPermissionsResult(String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                // 判断权限是否被拒绝
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[i])) {
                    // 申请授权
                    ActivityCompat.requestPermissions(mActivity, getNotGrantPermissions(), REQUEST_CODE_GRANT_PERMIT);
                    return false;
                }
            }
        }
        return true;
    }

}
