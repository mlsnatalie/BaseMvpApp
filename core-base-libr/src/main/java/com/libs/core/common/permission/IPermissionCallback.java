package com.libs.core.common.permission;

/**
 * @author NiYongliang on 2017/2/28.
 */

public interface IPermissionCallback {
    void onPermissionsResult(boolean result, String... permission);
}
