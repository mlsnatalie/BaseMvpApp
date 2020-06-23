package com.libs.core.common.utils;

import android.content.Context;

import com.libs.core.common.dialog.ToastDialog;

/**
 * Toast提示工具
 *
 * @author zhang.zheng
 * @version 2016-07-05
 */
public class ToastHelper {

//    private Toast mToast;

    private static volatile ToastHelper instance;

    private ToastHelper() {

    }

    public synchronized static ToastHelper getInstance() {
        if (instance == null) {
            synchronized (ToastHelper.class) {
                if (instance == null) {
                    instance = new ToastHelper();
                }
            }
        }
        return instance;
    }

    /**
     * open in top
     */
    public void showTop(Context context, String message) {
//        createToast(context, message, Gravity.TOP);
        ToastDialog.makeText(context, message, ToastDialog.LENGTH_SHORT).show();
    }

    /**
     * open in center
     */
    public void showCenter(Context context, String message) {
//        createToast(context, message, Gravity.CENTER);
        ToastDialog.makeText(context, message, ToastDialog.LENGTH_SHORT).show();
    }


    /**
     * open in bottom
     */
    public void showBottom(Context context, String message) {
//        createToast(context, message, Gravity.BOTTOM);
        ToastDialog.makeText(context, message, ToastDialog.LENGTH_SHORT).show();
    }


    /**
     * create toast
     */
//    private void createToast(Context context, String message, int gravity) {
//        if (context == null)
//            return;

//        View view = View.inflate(context, R.layout.custom_toast_view, null);
//        ((TextView) view).setText(message);
//        // 解决创建多个toast问题
//        if (mToast == null) {
//            mToast = new Toast(context);
//        }
//        switch (gravity) {
//            case Gravity.TOP:
//                mToast.setGravity(Gravity.TOP, 0, 250);
//                break;
//            case Gravity.BOTTOM:
//                mToast.setGravity(Gravity.BOTTOM, 0, 250);
//                break;
//            default:
//                mToast.setGravity(Gravity.CENTER, 0, 0);
//                break;
//        }
//        mToast.setDuration(Toast.LENGTH_SHORT);
//        mToast.setView(view);
//        mToast.isShow();
//    }


}

