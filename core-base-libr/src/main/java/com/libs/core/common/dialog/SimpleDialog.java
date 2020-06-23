package com.libs.core.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.libs.core.R;

/**
 * 自定义公共对话框-A
 *
 * @author zhang.zheng
 * @version 2017-09-20
 */
public class SimpleDialog {

    private Context mContext;
    private AlertDialog mAlertDialog;
    private Builder mBuilder;

    private int mTitleResId;
    private int mMessageResId;
    private int mMessageGragvity;
    private CharSequence mTitle;
    private CharSequence mMessage;
    private TextView mPositiveButton;
    private TextView mNegativeButton;

    private View mView;
    private View mMessageContentView;
    private int mMessageContentViewResId;
    private int mBackgroundResId = -1;
    private Drawable mBackgroundDrawable;

    private boolean mCancel;
    private boolean mHasShow = false;

    private String mPositiveText, mNegativeText;
    private int mPositiveId = -1, mNegativeId = -1;
    private View.OnClickListener mPositiveListener, mNegativeListener;
    private DialogInterface.OnDismissListener mOnDismissListener;


    public SimpleDialog(Context context) {
        this.mContext = context;
    }


    public void show() {
        if (!mHasShow) {
            mBuilder = new Builder(mContext);
        } else {
            mAlertDialog.show();
        }
        mHasShow = true;
    }


    public SimpleDialog setView(View view) {
        mView = view;
        if (mBuilder != null) {
            mBuilder.setView(view);
        }
        return this;
    }


    public SimpleDialog setContentView(View view) {
        mMessageContentView = view;
        mMessageContentViewResId = 0;
        if (mBuilder != null) {
            mBuilder.setContentView(mMessageContentView);
        }
        return this;
    }


    public SimpleDialog setContentView(int layoutResId) {
        mMessageContentViewResId = layoutResId;
        mMessageContentView = null;
        if (mBuilder != null) {
            mBuilder.setContentView(layoutResId);
        }
        return this;
    }

    public SimpleDialog setBackground(Drawable drawable) {
        mBackgroundDrawable = drawable;
        if (mBuilder != null) {
            mBuilder.setBackground(mBackgroundDrawable);
        }
        return this;
    }

    public SimpleDialog setBackgroundResource(int resId) {
        mBackgroundResId = resId;
        if (mBuilder != null) {
            mBuilder.setBackgroundResource(mBackgroundResId);
        }
        return this;
    }

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    private static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    public SimpleDialog setTitle(int resId) {
        mTitleResId = resId;
        if (mBuilder != null) {
            mBuilder.setTitle(resId);
        }
        return this;
    }


    public SimpleDialog setTitle(CharSequence title) {
        mTitle = title;
        if (mBuilder != null) {
            mBuilder.setTitle(title);
        }
        return this;
    }


    public SimpleDialog setMessage(int resId) {
        mMessageResId = resId;
        if (mBuilder != null) {
            mBuilder.setMessage(resId);
        }
        return this;
    }


    public SimpleDialog setMessage(CharSequence message) {
        mMessage = message;
        if (mBuilder != null) {
            mBuilder.setMessage(message);
        }
        return this;
    }

    public SimpleDialog setMessageGravity(int gravity) {
        mMessageGragvity = gravity;
        return this;
    }


    public SimpleDialog setPositiveButton(int resId, final View.OnClickListener listener) {
        this.mPositiveId = resId;
        this.mPositiveListener = listener;
        return this;
    }


    public TextView getPositiveButton() {
        return mPositiveButton;
    }


    public TextView getNegativeButton() {
        return mNegativeButton;
    }


    public SimpleDialog setPositiveButton(String text, final View.OnClickListener listener) {
        this.mPositiveText = text;
        this.mPositiveListener = listener;
        return this;
    }


    public SimpleDialog setNegativeButton(int resId, final View.OnClickListener listener) {
        this.mNegativeId = resId;
        this.mNegativeListener = listener;
        return this;
    }


    public SimpleDialog setNegativeButton(String text, final View.OnClickListener listener) {
        this.mNegativeText = text;
        this.mNegativeListener = listener;
        return this;
    }


    public SimpleDialog setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        if (mBuilder != null) {
            mBuilder.setCanceledOnTouchOutside(mCancel);
        }
        return this;
    }


    public SimpleDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    //新增 2.5.2 start
    public TextView getTitleView() { //需在 show 之后调用
        return mBuilder.mTitleView;
    }

    public TextView getContentView() {
        return mBuilder.mMessageView;
    }

    public View getTopLineView() {
        return mBuilder.view_top_line;
    }

    //新增 2.5.2 end
    private class Builder {

        private Window mAlertDialogWindow;
        private LinearLayout mDialogRootView;
        private LinearLayout mTitleContentView;
        private TextView mTitleView;
        private ViewGroup mContentRootView;
        private TextView mMessageView;
        private LinearLayout mActionButtonView;
        private View mSplitLineView;

        private View view_top_line;

        private Builder(Context context) {
            mAlertDialog = new AlertDialog.Builder(context).create();
            mAlertDialog.show();

            mAlertDialogWindow = mAlertDialog.getWindow();
            if (mAlertDialogWindow != null) {
                mAlertDialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                mAlertDialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
                mAlertDialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mAlertDialogWindow.setBackgroundDrawableResource(R.drawable.material_dialog_window_bg);

                View contentView = View.inflate(mContext, R.layout.custom_simple_dialog_view, null);
                contentView.setFocusable(true);
                contentView.setFocusableInTouchMode(true);
                mAlertDialogWindow.addContentView(contentView,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                view_top_line = contentView.findViewById(R.id.view_top_line);
                view_top_line.setVisibility(View.GONE);

                mDialogRootView = mAlertDialogWindow.findViewById(R.id.dialog_root_view);
                mTitleContentView = mDialogRootView.findViewById(R.id.title_content_view);
                mTitleView = mTitleContentView.findViewById(R.id.title);
                mContentRootView = mAlertDialogWindow.findViewById(R.id.content_root_view);
                mMessageView = mAlertDialogWindow.findViewById(R.id.message);
                // 底部按钮
                mActionButtonView = mAlertDialogWindow.findViewById(R.id.action_button_view);
                mNegativeButton = mActionButtonView.findViewById(R.id.negative_btn);
                mPositiveButton = mActionButtonView.findViewById(R.id.positive_btn);
                mSplitLineView = mAlertDialogWindow.findViewById(R.id.split_line_view);

                if (mView != null) {
                    mTitleContentView.removeAllViews();
                    mTitleContentView.addView(mView);
                }
                if (mTitleResId != 0) {
                    setTitle(mTitleResId);
                }
                if (mTitle != null) {
                    setTitle(mTitle);
                }
                if (mTitle == null && mTitleResId == 0) {
                    mTitleView.setVisibility(View.GONE);
                }
                if (mMessageResId != 0) {
                    setMessage(mMessageResId);
                }
                if (mMessage != null) {
                    setMessage(mMessage);
                    if (mMessageGragvity > 0) {
                        mMessageView.setGravity(mMessageGragvity);
                    }
                }

                if (mPositiveId != -1) {
                    mPositiveButton.setVisibility(View.VISIBLE);
                    mPositiveButton.setText(mPositiveId);
                    mPositiveButton.setOnClickListener(mPositiveListener);
                    if (isLollipop()) {
                        mPositiveButton.setElevation(0);
                    }
                }
                if (mNegativeId != -1) {
                    mNegativeButton.setVisibility(View.VISIBLE);
                    mNegativeButton.setText(mNegativeId);
                    mNegativeButton.setOnClickListener(mNegativeListener);
                    if (isLollipop()) {
                        mNegativeButton.setElevation(0);
                    }
                }
                // 控制分割线显示/隐藏
                if ((mPositiveId != -1 && mNegativeId != -1) || (!TextUtils.isEmpty(mPositiveText) &&
                        !TextUtils.isEmpty(mNegativeText))) {
                    mSplitLineView.setVisibility(View.VISIBLE);
                } else {
                    mSplitLineView.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(mPositiveText)) {
                    mPositiveButton.setVisibility(View.VISIBLE);
                    mPositiveButton.setText(mPositiveText);
                    mPositiveButton.setOnClickListener(mPositiveListener);
                    if (isLollipop()) {
                        mPositiveButton.setElevation(0);
                    }
                }

                if (!TextUtils.isEmpty(mNegativeText)) {
                    mNegativeButton.setVisibility(View.VISIBLE);
                    mNegativeButton.setText(mNegativeText);
                    mNegativeButton.setOnClickListener(mNegativeListener);
                    if (isLollipop()) {
                        mNegativeButton.setElevation(0);
                    }
                }
                if (TextUtils.isEmpty(mPositiveText) && mPositiveId == -1) {
                    mPositiveButton.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(mNegativeText) && mNegativeId == -1) {
                    mNegativeButton.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(mPositiveText) && mPositiveId == -1
                        && TextUtils.isEmpty(mNegativeText) && mNegativeId == -1){
                    mActionButtonView.setVisibility(View.GONE);
                }

                if (mBackgroundResId != -1) {
                    mDialogRootView.setBackgroundResource(mBackgroundResId);
                }
                if (mBackgroundDrawable != null) {
                    mDialogRootView.setBackground(mBackgroundDrawable);
                }

                if (mMessageContentView != null) {
                    this.setContentView(mMessageContentView);
                } else if (mMessageContentViewResId != 0) {
                    this.setContentView(mMessageContentViewResId);
                }
                mAlertDialog.setCanceledOnTouchOutside(mCancel);
                mAlertDialog.setCancelable(mCancel);
                if (mOnDismissListener != null) {
                    mAlertDialog.setOnDismissListener(mOnDismissListener);
                }
            }
        }

        public void setTitle(int resId) {
            mTitleView.setText(resId);
        }

        public void setTitle(CharSequence title) {
            mTitleView.setText(title);
        }

        public void setMessage(int resId) {
            if (mMessageView != null) {
                mMessageView.setText(resId);
            }
        }

        public void setMessage(CharSequence message) {
            if (mMessageView != null) {
                mMessageView.setText(message);
            }
        }

        /**
         * set positive button
         *
         * @param text the type of button
         */
        public void setPositiveButton(String text, final View.OnClickListener listener) {
            TextView button = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.
                    LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setText(text);
            button.setOnClickListener(listener);
            mActionButtonView.addView(button);
        }

        /**
         * set negative button
         *
         * @param text the type of button
         */
        public void setNegativeButton(String text, final View.OnClickListener listener) {
            TextView button = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.
                    LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setText(text);
            button.setOnClickListener(listener);
            if (mActionButtonView.getChildCount() > 0) {
                button.setLayoutParams(params);
                mActionButtonView.addView(button, 1);
            } else {
                button.setLayoutParams(params);
                mActionButtonView.addView(button);
            }
        }

        public void setView(View view) {
            mTitleContentView.removeAllViews();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    mAlertDialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    // open imm
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            });

            mTitleContentView.addView(view);

            if (view instanceof ViewGroup) {

                ViewGroup viewGroup = (ViewGroup) view;

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof EditText) {
                        EditText editText = (EditText) viewGroup.getChildAt(i);
                        editText.setFocusable(true);
                        editText.requestFocus();
                        editText.setFocusableInTouchMode(true);
                    }
                }
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof AutoCompleteTextView) {
                        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) viewGroup
                                .getChildAt(i);
                        autoCompleteTextView.setFocusable(true);
                        autoCompleteTextView.requestFocus();
                        autoCompleteTextView.setFocusableInTouchMode(true);
                    }
                }
            }
        }

        public void setContentView(View contentView) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(layoutParams);
            if (contentView instanceof ListView) {
                setListViewHeightBasedOnChildren((ListView) contentView);
            }
            LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id.content_message_view);
            if (linearLayout != null) {
                linearLayout.removeAllViews();
                linearLayout.addView(contentView);
            }
            for (int i = 0; i < (linearLayout != null ? linearLayout.getChildCount() : 0); i++) {
                if (linearLayout.getChildAt(i) instanceof AutoCompleteTextView) {
                    AutoCompleteTextView autoCompleteTextView
                            = (AutoCompleteTextView) linearLayout.getChildAt(i);
                    autoCompleteTextView.setFocusable(true);
                    autoCompleteTextView.requestFocus();
                    autoCompleteTextView.setFocusableInTouchMode(true);
                }
            }
        }

        /**
         * Set a custom view resource to be the contents of the dialog. The
         * resource will be inflated into a ScrollView.
         *
         * @param layoutResId resource ID to be inflated
         */
        public void setContentView(int layoutResId) {
            mContentRootView.removeAllViews();
            // Not setting this to the other content view because user has defined their own
            // layout params, and we don't want to overwrite those.
            LayoutInflater.from(mContentRootView.getContext()).inflate(layoutResId, mContentRootView);
        }

        public void setBackground(Drawable drawable) {
            LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id.dialog_root_view);
            linearLayout.setBackground(drawable);
        }

        public void setBackgroundResource(int resId) {
            LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id.dialog_root_view);
            linearLayout.setBackgroundResource(resId);
        }

        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            mAlertDialog.setCancelable(canceledOnTouchOutside);
        }
    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public AlertDialog getDialog() {
        return mAlertDialog;
    }
}

