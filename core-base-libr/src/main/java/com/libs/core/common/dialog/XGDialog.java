package com.libs.core.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libs.core.R;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : amos
 *     e-mail : hui.li1@yintech.cn
 *     time   : 2019/09/11 18:36
 *     desc   : 西瓜公共dialog
 *     version: 1.0
 * </pre>
 */
public class XGDialog {
    private Builder builder;
    //private AlertDialog mAlertDialog;
    private Dialog mDialog;
    private Window mWindow;
    private static Map<Context, AlertDialog> dialogMap = new HashMap<>();

    private XGDialog(Builder builder) {
        this.builder = builder;
    }

    private View initDialog() {
        if (builder.getRootView() != null) {
            return builder.getRootView();
        }
        View rootView = View.inflate(builder.getContext(), R.layout.dialog_common, null);
        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true);
        //标题
        TextView mTitleView = rootView.findViewById(R.id.title);
        //标题下的分隔线
        View view_top_line = rootView.findViewById(R.id.view_top_line);
        view_top_line.setVisibility(View.GONE);
        //中间内容view
        LinearLayout content_message_view = rootView.findViewById(R.id.content_message_view);
        //内容
        TextView mMessageView = rootView.findViewById(R.id.message);
        // 底部按钮
        TextView mNegativeButton = rootView.findViewById(R.id.negative_btn);
        TextView mPositiveButton = rootView.findViewById(R.id.positive_btn);
        View mSplitLineView = rootView.findViewById(R.id.split_line_view);

        //添加数据
        String title = builder.getTitle();
        if (TextUtils.isEmpty(title)) {
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setVisibility(View.VISIBLE);
            mTitleView.setText(title);
        }
        view_top_line.setVisibility(builder.isShowTopLine() ? View.VISIBLE : View.GONE);
        if (builder.getContentView() != null) {
            content_message_view.removeAllViews();
            content_message_view.addView(builder.getContentView());
        } else {
            mMessageView.setText(TextUtils.isEmpty(builder.getContent()) ? "" : builder.getContent());
        }

        mNegativeButton.setText(TextUtils.isEmpty(builder.getNegativeWord()) ? "" : builder.getNegativeWord());
        mPositiveButton.setText(TextUtils.isEmpty(builder.getPositiveWord()) ? "" : builder.getPositiveWord());

        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (builder.getNegativeListener() != null) {
                    builder.getNegativeListener().onClick(v);
                }
            }
        });

        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (builder.getPositiveListener() != null) {
                    builder.getPositiveListener().onClick(v);
                }
            }
        });
        try {
            if (builder.getNegativeBtnTextColor() > 0) {
                mNegativeButton.setTextColor(builder.getNegativeBtnTextColor());
            }
            if (builder.getPositiveBtnTextColor() > 0) {
                mPositiveButton.setTextColor(builder.getPositiveBtnTextColor());
            }
        } catch (Exception e) {

        }
        return rootView;
    }

    public void show() {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        View contentView = initDialog();
        mDialog =  new Dialog(builder.getContext(),R.style.XG_Common_Dialog_Style);
        mDialog.setContentView(contentView);
        boolean cancel = builder.isOutSide();
        mDialog.setCanceledOnTouchOutside(cancel);
        mDialog.setCancelable(cancel);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (builder.getOnDismissListener() != null) {
                    builder.onDismissListener.onDismiss(dialog);
                }
            }
        });

        mWindow = mDialog.getWindow();
        if (mWindow != null) {
            mWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            mWindow.setGravity(Gravity.CENTER);
            if(mWindow.getDecorView() != null){
                mWindow.getDecorView().setPadding(0,0,0,0);
            }
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
           // mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //mWindow.setBackgroundDrawableResource(R.drawable.material_dialog_window_bg);
        }
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public boolean isShowing(){
        if(mDialog != null &&mDialog.isShowing()){
            return true;
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private String positiveWord;
        private String negativeWord;
        private View.OnClickListener positiveListener;
        private View.OnClickListener negativeListener;
        private boolean isOutSide;
        private View contentView; //中间布局
        private View rootView; //自定义整个布局
        private int positiveBtnTextColor;
        private int negativeBtnTextColor;
        private boolean isShowTopLine;
        private DialogInterface.OnDismissListener onDismissListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getContent() {
            return content;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public String getPositiveWord() {
            return positiveWord;
        }

        public Builder setPositiveWord(String positiveWord) {
            this.positiveWord = positiveWord;
            return this;
        }

        public String getNegativeWord() {
            return negativeWord;
        }

        public Builder setNegativeWord(String negativeWord) {
            this.negativeWord = negativeWord;
            return this;
        }

        public View.OnClickListener getPositiveListener() {
            return positiveListener;
        }

        public Builder setPositiveListener(View.OnClickListener positiveListener) {
            this.positiveListener = positiveListener;
            return this;
        }

        public View.OnClickListener getNegativeListener() {
            return negativeListener;
        }

        public Builder setNegativeListener(View.OnClickListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        public boolean isOutSide() {
            return isOutSide;
        }

        public Builder setOutSide(boolean outSide) {
            isOutSide = outSide;
            return this;
        }

        public View getContentView() {
            return contentView;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public View getRootView() {
            return rootView;
        }

        public Builder setRootView(View rootView) {
            this.rootView = rootView;
            return this;
        }

        public int getPositiveBtnTextColor() {
            return positiveBtnTextColor;
        }

        public Builder setPositiveBtnTextColor(int positiveBtnTextColor) {
            this.positiveBtnTextColor = positiveBtnTextColor;
            return this;
        }

        public int getNegativeBtnTextColor() {
            return negativeBtnTextColor;
        }

        public Builder setNegativeBtnTextColor(int negativeBtnTextColor) {
            this.negativeBtnTextColor = negativeBtnTextColor;
            return this;
        }

        public boolean isShowTopLine() {
            return isShowTopLine;
        }

        public Builder setShowTopLine(boolean showTopLine) {
            isShowTopLine = showTopLine;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public DialogInterface.OnDismissListener getOnDismissListener() {
            return onDismissListener;
        }

        public XGDialog create() {

            return new XGDialog(this);
        }
    }

}
