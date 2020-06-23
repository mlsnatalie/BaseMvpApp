package com.libs.core.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.libs.core.R;

public class ToastDialog {

    public static final int LENGTH_LONG = 3000;
    public static final int LENGTH_SHORT = 1500;

    private Context mContext;
    private Handler mHandler;
    private TextView mTextView;
    private int mDuration;
    private Dialog dialog;

    private ToastDialog(Context context) {
        try {
            mContext = context;
            mHandler = new Handler();
            dialog = new Dialog(mContext, R.style.XToastDialogStyle);
            dialog.setContentView(R.layout.toast_dialog_view);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            dialog.setCanceledOnTouchOutside(true);
            mTextView = (TextView) dialog.findViewById(R.id.message_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ToastDialog makeText(Context context, CharSequence message, int duration) {
        ToastDialog toastUtils = new ToastDialog(context);
        try {
            toastUtils.mDuration = duration;
            toastUtils.mTextView.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toastUtils;
    }

    public static ToastDialog makeText(Context context, int resId, int duration) {
        String mes = "";
        try {
            mes = context.getResources().getString(resId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return makeText(context, mes, duration);
    }


    public void show() {
        try {
            dialog.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, mDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
