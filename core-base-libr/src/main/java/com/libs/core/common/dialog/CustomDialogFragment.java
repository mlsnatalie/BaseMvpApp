package com.libs.core.common.dialog;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.libs.core.BuildConfig;
import com.libs.core.R;
import com.libs.core.common.base.BaseRxDialogFragment;

public class CustomDialogFragment extends BaseRxDialogFragment implements View.OnClickListener {

    public static final String PRARM_TITLE = "title";
    public static final String PRARM_MESSAGE = "message";

    TextView mTitle;

    TextView mMessage;

    TextView mOk;

    TextView mCancel;

    String mOkStr, mCancelStr;
    View.OnClickListener mPostiveButtonLisr, mNegetiveButtonLisr;

    @Override
    protected int initLayout() {
        return R.layout.custom_simple_dialog_view_left;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViewData(Bundle bundle) {
        Bundle data = getArguments();
        if (data == null) {
            getDialog().cancel();
            return;
        }
        mTitle = mRootView.findViewById(R.id.title);
        mMessage = mRootView.findViewById(R.id.message);
        mOk = mRootView.findViewById(R.id.positive_btn);
        mCancel = mRootView.findViewById(R.id.negative_btn);

        String title = data.getString(PRARM_TITLE, "");
        String msg = data.getString(PRARM_MESSAGE, "");
        mTitle.setText(title);
        mMessage.setText(msg);

        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        if (!TextUtils.isEmpty(mOkStr)) {
            mOk.setText(mOkStr);
        } else {
            mOk.setText("确定");
        }


        if (!TextUtils.isEmpty(mCancelStr)) {
            mCancel.setText(mCancelStr);
        } else {
            mCancel.setText("取消");
        }

        if (mNegetiveButtonLisr == null) {
            //如果没有取消按钮，则把取消按钮隐藏
            mCancel.setVisibility(View.GONE);
            //隐藏竖线
            mRootView.findViewById(R.id.split_line_view).setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(String text, View.OnClickListener listener) {
        mPostiveButtonLisr = listener;
        mOkStr = text;
    }

    public void setNegetiveButton(String text, View.OnClickListener listener) {
        mNegetiveButtonLisr = listener;
        mCancelStr = text;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.positive_btn) {
            if (mPostiveButtonLisr != null)
                mPostiveButtonLisr.onClick(mOk);
            if (BuildConfig.DEBUG) {
                ClipboardManager cm = (ClipboardManager) (getContext().getSystemService(Context.CLIPBOARD_SERVICE));
                cm.setText(mMessage.getText());
               showToast("复制成功" + mMessage.getText());
            }
            getDialog().cancel();
        } else if (v.getId() == R.id.negative_btn) {
            if (mNegetiveButtonLisr != null)
                mNegetiveButtonLisr.onClick(mCancel);
            getDialog().cancel();
        }
    }
}
