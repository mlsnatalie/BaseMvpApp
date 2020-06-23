package com.libs.core.common.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libs.core.R;
import com.libs.core.common.utils.AppUtils;
import com.libs.core.common.utils.UiHelper;

import java.util.Objects;

import io.reactivex.disposables.Disposable;


public abstract class BaseDialog<T extends BaseDialog> extends Dialog implements View.OnClickListener {


    protected Context mContext;
    protected boolean canBack = true;
    protected TextView mTitleView;
    protected LinearLayout mFlContent;
    protected TextView leftView;
    protected TextView rightView;
    protected LinearLayout mRootView;
    protected View mBottomView;
    protected View line2;
    protected BtnClickListener<T> rightClick;
    protected BtnClickListener<T> leftClick;
    protected T mDialog;
    protected boolean showFullScreen;
    protected int customHeight = 0;

    private Object holder;
    protected TextClickListener listener;
    protected int mRightCountDown;
    protected int mLeftCountDown;
    private String mLeftBtnText;
    private String mRightBtnText;
    private Disposable mRightDisposable;
    private Disposable mLeftDisposable;
    private boolean mCancelable;


    public BaseDialog(@NonNull Context context) {
        this(context, R.style.defaultDialogStyle);
        this.mContext = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mDialog = (T) this;
        initDialog();
    }

    protected final <T extends View> T $(@IdRes int id) {
        return (T) (findViewById(id));
    }


    public T hideSystemUI() {
        showFullScreen = true;
        return mDialog;
    }

    private void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //无标题
        // 透明背景
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_l_title_dialog);

        Window window = getWindow();

        WindowManager.LayoutParams attributes = Objects.requireNonNull(window).getAttributes();
        window.getDecorView().setPadding(0, 0, 0, 0);
        if (setCustomWidth() != 0) {
            attributes.width = AppUtils.dp2px(setCustomWidth());
        } else {
            attributes.width = (int) getContext().getResources().getDimension(R.dimen.base_dialog_width);
        }
        attributes.gravity = Gravity.CENTER;
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.verticalMargin = 0;
        if (setCustomHeight() != 0) {
            attributes.height = AppUtils.dp2px(setCustomHeight());
        }
        window.setAttributes(attributes);
//        if (!isInputModel()) {
//            mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        }


        setCanceledOnTouchOutside(true); //点击空白不取消
        setCancelable(canBack); //点击返回按钮不取消
        mRootView = (LinearLayout) findViewById(R.id.ll_dialog_base);
        mTitleView = (TextView) findViewById(R.id.tv_dialog_title);
        mFlContent = (LinearLayout) findViewById(R.id.fl_dialog_content);
        mBottomView = findViewById(R.id.fl_dialog_bottom);
        leftView = (TextView) findViewById(R.id.txt_to_left);
        rightView = (TextView) findViewById(R.id.txt_to_right);
        line2 = findViewById(R.id.line2);

        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);


        initView(mContext);
        if (!customRadios()) {
            UiHelper.radiosView(mRootView, 15);
        }
        // mRootView.invalidate();


    }

    protected void addContent(View view) {
        mFlContent.addView(view);
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (mWindow != null && !isInputModel()) {
//            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        }
//    }

    protected boolean customRadios() {
        return false;
    }

    protected abstract void initView(Context context);


    public T changeDialogHeight(int customHeight) {
        if (customHeight > 0) {
            this.customHeight = customHeight;
        }
        return mDialog;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txt_to_left) {
            if (leftClick == null) {
                dismiss();
            } else {
                leftClick.onClick(mDialog);
            }
        } else if (id == R.id.txt_to_right) {
            if (rightClick == null) {
                dismiss();
            } else {
                rightClick.onClick(mDialog);
            }
        }
    }


    @NonNull
    protected LinearLayout.LayoutParams getLayoutParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }

        setHeight();//微调dialog高度到不了720
    }


    private void setHeight() {

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        // LogUtil.d("BaseDialog","Height = " + window.getDecorView().getHeight() + " MAX = " +AppUtils.dp2px(670));
//        if (BuildConfig.IS_LAND_MODEL) {
//
//            if (window.getDecorView().getHeight() >= AppUtils.dp2px(670)) {
//                attributes.height = AppUtils.dp2px(720);
//            }
//        }

        if (setCustomHeight() != 0) {
            attributes.height = AppUtils.dp2px(setCustomHeight());
        }

        window.setAttributes(attributes);

    }


    public int setCustomHeight() {
        return customHeight;
    }

    public int setCustomWidth() {
        return 0;
    }


    public T setLeftBtn(int stringRes) {
        mBottomView.setVisibility(View.VISIBLE);
        leftView.setVisibility(View.VISIBLE);
        leftView.setText(AppUtils.getString(stringRes));
        return mDialog;
    }

    public T setLeftBtn(int stringRes, @Nullable BtnClickListener<T> listener) {
        mBottomView.setVisibility(View.VISIBLE);
        leftView.setVisibility(View.VISIBLE);
        leftView.setText(AppUtils.getString(stringRes));
        leftClick = listener;
        return mDialog;
    }

    public T setLeftBtn(String stringRes, @Nullable BtnClickListener<T> listener) {
        mBottomView.setVisibility(View.VISIBLE);
        leftView.setVisibility(View.VISIBLE);
        leftView.setText(stringRes);
        leftClick = listener;
        return mDialog;
    }

    public T setRightBtn(int stringRes) {
        mBottomView.setVisibility(View.VISIBLE);
        rightView.setVisibility(View.VISIBLE);
        rightView.setText(AppUtils.getString(stringRes));
        return mDialog;
    }

    public T setRightBtn(int stringRes, @Nullable BtnClickListener<T> listener) {
        mBottomView.setVisibility(View.VISIBLE);
        rightView.setVisibility(View.VISIBLE);
        rightView.setText(AppUtils.getString(stringRes));
        rightClick = listener;
        return mDialog;
    }

    public T setRightBtn(int stringRes, int color, @Nullable BtnClickListener<T> listener) {
        mBottomView.setVisibility(View.VISIBLE);
        rightView.setVisibility(View.VISIBLE);
        rightView.setText(AppUtils.getString(stringRes));
        rightView.setTextColor(AppUtils.getColor(color));
        rightClick = listener;
        return mDialog;
    }

    public T setOneBtnModel(int stringRes, @Nullable BtnClickListener<T> listener) {
        mBottomView.setVisibility(View.VISIBLE);
        rightView.setVisibility(View.VISIBLE);
        leftView.setVisibility(View.GONE);
        rightView.setText(AppUtils.getString(stringRes));
        line2.setVisibility(View.INVISIBLE);
        rightClick = listener;
        return mDialog;
    }

    public T setNoneBtnModel() {
        leftView.setVisibility(View.GONE);
        rightView.setVisibility(View.GONE);
        line2.setVisibility(View.INVISIBLE);
        mBottomView.setVisibility(View.GONE);
        rightView.setVisibility(View.VISIBLE);
        return mDialog;
    }

    public T setOneBtnModel(int stringRes) {
        mBottomView.setVisibility(View.VISIBLE);
        leftView.setVisibility(View.GONE);
        rightView.setText(AppUtils.getString(stringRes));
        line2.setVisibility(View.INVISIBLE);
        rightView.setVisibility(View.VISIBLE);
        return mDialog;
    }


    public T noBtnModel() {
        mBottomView.setVisibility(View.GONE);
        return mDialog;
    }

    public interface BtnClickListener<T> {
        void onClick(T dialog);
    }

    public void setBtnClickListener(BtnClickListener leftClick, BtnClickListener rightClick) {
        mBottomView.setVisibility(View.VISIBLE);
        this.leftClick = leftClick;
        this.rightClick = rightClick;
    }
//    public interface ClickListener<T> extends BtnClickListener {
//        void onCancel(T dialog);
//    }

    @Override
    public void dismiss() {

//        if (!SunmiDialog.hideAllDialog)
//            SunmiDialog.remove(this);
        super.dismiss();

        if (listener != null) {
            listener.onTextClick();
        }
        if (rightClick != null) {
            rightClick = null;
        }

        if (mLeftDisposable != null)
            mLeftDisposable.dispose();


        if (mRightDisposable != null)
            mRightDisposable.dispose();

        if (leftClick != null) {
            leftClick = null;
        }

        if (mContext != null) {
            mContext = null;
        }
//        try {
//            if (LoadingDialog.getLoadingDialog() != null) {
//                LoadingDialog.getLoadingDialog().dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    /**
     * @param listener
     * @return 这个是用来 监听dialog消失事件
     */
    public T listenDismiss(TextClickListener listener) {
        this.listener = listener;
        return mDialog;
    }

    public T setCanBack(boolean cancelable) {
        canBack = cancelable;
        setCancelable(canBack);
        return mDialog;
    }

    public T backgroundWhite() {
        mRootView.setBackgroundColor(AppUtils.getColor(R.color.white));
        return mDialog;
    }

    //设置右边按钮倒计时
    public T setRightBtnCountdown(int secends) {
        if (secends > 0)
            mRightCountDown = secends;
        return mDialog;
    }

    //设置左边按钮倒计时
    public T setLeftBtnCountdown(int secends) {
        if (secends > 0)
            mLeftCountDown = secends;
        return mDialog;
    }


    public void changeRightBtnClickStatus(boolean canClick) {
        mBottomView.setVisibility(View.VISIBLE);
        rightView.setTextColor(canClick ? AppUtils.getColor(R.color.FF333C4F) : AppUtils.getColor(R.color.GG333C4F));
        rightView.setClickable(canClick);
    }


    public void changeLeftBtnClickStatus(boolean canClick) {
        mBottomView.setVisibility(View.VISIBLE);
        leftView.setTextColor(canClick ? AppUtils.getColor(R.color.FF333C4F) : AppUtils.getColor(R.color.GG333C4F));
        leftView.setClickable(canClick);
    }


    /**
     * @param holder 给需要鉴别dialog类型预留参数
     * @return
     */
    public T setHolder(Object holder) {
        this.holder = holder;
        return mDialog;
    }


    @Override
    public void show() {
        if (showFullScreen)
            Objects.requireNonNull(this.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


        beforeShow();


        super.show();

        afterShow();

        if (showFullScreen) {
            fullScreenImmersive(Objects.requireNonNull(getWindow()).getDecorView());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void afterShow() {


//        if (mRightCountDown > 0) {
//            mRightDisposable = Flowable.intervalRange(0, mRightCountDown + 1, 0, 1, TimeUnit.SECONDS)
//                    .compose(RxUtil.rxFlowableSchedulerHelper())
//                    .subscribe(aLong -> {
//                        LogUtil.d(getClass().getSimpleName(), "mRightCountDown = " + mRightCountDown + " : aLong = " + aLong);
//                        if (rightView != null) {
//                            rightView.setText(mRightBtnText + "(" + (mRightCountDown - aLong) + "S)");
//                        }
//                    }, throwable -> {
//                        if (rightView != null) {
//                            rightView.setText(mRightBtnText);
//                        }
//                    }, () -> {
//                        LogUtil.d(getClass().getSimpleName(), "mRightCountDown = " + mRightCountDown + " : onComplete");
//
//                        if (rightView != null) {
//                            rightView.setText(mRightBtnText);
//                            changeRightBtnClickStatus(true);
//                        }
//                    });
//
//        }
//
//
//        if (mLeftCountDown > 0) {
//            mLeftDisposable = Flowable.intervalRange(0, mLeftCountDown + 1, 0, 1, TimeUnit.SECONDS)
//                    .compose(RxUtil.rxFlowableSchedulerHelper())
//                    .subscribe(aLong -> {
//                        if (leftView != null) {
//                            leftView.setText(mLeftBtnText + "(" + (mLeftCountDown - aLong) + "S)");
//                        }
//                    }, throwable -> {
//                        if (leftView != null) {
//                            leftView.setText(mLeftBtnText);
//                        }
//                    }, () -> {
//                        if (leftView != null) {
//                            leftView.setText(mLeftBtnText);
//                            changeLeftBtnClickStatus(true);
//                        }
//                    });
//
//        }


    }

    private void beforeShow() {
        if (mRightCountDown > 0) {
            changeRightBtnClickStatus(false);
            mRightBtnText = rightView.getText().toString();
        }


        if (mLeftCountDown > 0) {
            changeLeftBtnClickStatus(false);
            mLeftBtnText = leftView.getText().toString();
        }


    }

    public T setTitleText(int stringRes) {
        mTitleView.setText(AppUtils.getString(stringRes));
        return mDialog;
    }


    @Override
    public void setCancelable(boolean flag) {
        mCancelable = flag;
        super.setCancelable(flag);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (mCancelable && !isTouchPointInView(mRootView, (int) event.getRawX(), (int) event.getRawY())) {
            dismiss();
            return true;
        }
        return super.onTouchEvent(event);
    }


    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

    public Object getHolder() {
        return holder;
    }
}
