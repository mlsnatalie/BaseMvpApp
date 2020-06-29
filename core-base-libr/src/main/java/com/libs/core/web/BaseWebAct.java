package com.libs.core.web;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.libs.core.common.base.BasePresenter;
import com.libs.core.common.base.BaseRxActivity;
import com.libs.core.common.dialog.BaseDialog;
import com.libs.core.common.dialog.NotificationDialog;
import com.libs.core.common.utils.AppUtils;

/**
 * @author zhangxiaowei 2019-10-14
 */
public abstract class BaseWebAct<P extends BasePresenter> extends BaseRxActivity<P> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showNotificationDialog();
    }
    /**
     * 提示对话框
     */
    private void showNotificationDialog() {
        if (!AppUtils.isNotificationEnabled(mContext)) {
            if (AppUtils.overWeek()) {
                NotificationDialog dialog = new NotificationDialog(this);
                dialog.setLeftBtn("", new BaseDialog.BtnClickListener<NotificationDialog>() {
                    @Override
                    public void onClick(NotificationDialog dialog) {
                        dialog.dismiss();
                    }
                }).setRightBtn(0, new BaseDialog.BtnClickListener<NotificationDialog>() {
                    @Override
                    public void onClick(NotificationDialog dialog) {
                        AppUtils.settingNotification();
                        dialog.dismiss();
                    }
                }).show();
            }
        }

    }
}