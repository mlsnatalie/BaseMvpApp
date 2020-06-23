package com.libs.core.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.libs.core.R;

/**
 * 加载框工具类
 *
 * @author zhang.zheng
 * @version 2017-04-14
 */
public class LoadingDialog {


    /**
     * 创建加载框
     */
    public static Dialog createLoadingDialog(Context context, String message) {
        Dialog dialog = new Dialog(context, R.style.Custom_Loading_Style);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_loading_view);
        dialog.setCanceledOnTouchOutside(false);
        TextView textView =  dialog.findViewById(R.id.loading_text);
        if (TextUtils.isEmpty(message)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
        }
        return dialog;
    }


}
