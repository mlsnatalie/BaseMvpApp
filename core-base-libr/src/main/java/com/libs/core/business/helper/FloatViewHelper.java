package com.libs.core.business.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libs.core.R;
import com.libs.core.business.consts.PreferConst;
import com.libs.core.business.events.UserEvent;
import com.libs.core.business.sensor.SensorsTracker;
import com.libs.core.common.manager.PreferenceManager;
import com.libs.core.common.rxbus.RxBus;
import com.libs.core.common.utils.DensityUtils;
import com.libs.core.common.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class FloatViewHelper {

    private View mFloatView;
    private Context mContext;
    private ImageView mImageView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

    // 视图是否显示
    private boolean mIsShow;
    // 视图是否添加
    private boolean mIsAdded;
    // 视图是否折叠
    private boolean mIsFold;
    // 是否有未读消息
    private boolean mHasUnReadMsg;

    private int mDownX;
    private int mDownY;


    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mTimerDisposable;

    private void addSubscription(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            mCompositeDisposable.add(disposable);
        }
    }

    private void removeSubscription(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            mCompositeDisposable.remove(disposable);
        }
    }

    private void clearSubscription() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }


    public FloatViewHelper(Context context) {
        mContext = context;
        init();
    }

    public void setShow(boolean show) {
        mIsShow = show;
        if (mIsShow) {
            add();
        } else {
            remove();
        }
    }


    private void init() {
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.gravity = Gravity.END | Gravity.BOTTOM;
        mParams.x = 0;
        int yHeight = DensityUtils.dp2px(mContext, 190);
        mParams.y = PreferenceManager.getInt(PreferConst.FLOAT_VIEW_Y, yHeight);

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mFloatView = View.inflate(mContext, R.layout.float_customer_view, null);
        mImageView = mFloatView.findViewById(R.id.image_view);
        // 拖动事件
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            private int lastX;
            private int lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = lastX = (int) event.getRawX();
                        mDownY = lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int disX = (int) (lastX - event.getRawX());
                        int disY = (int) (lastY - event.getRawY());
                        // 仅支持垂直拖动
                        // mParams.x += disX;
                        mParams.y += disY;
                        int minY = DensityUtils.dp2px(mContext, 50);
                        if (mParams.y < minY) {
                            mParams.y = minY;
                        }
                        mWindowManager.updateViewLayout(mFloatView, mParams);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        int upX = mDownX - x;
                        int upY = mDownY - y;
                        upX = Math.abs(upX);
                        upY = Math.abs(upY);
                        if (upX < 20 && upY < 20) {
                            // 调用点击事件
                            mFloatView.performClick();
                        }
                        // 保存上次视图位置
                        PreferenceManager.putInt(PreferConst.FLOAT_VIEW_Y, mParams.y);
                        break;
                }
                return true;
            }
        });

        // 点击事件
        mFloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsTracker.getInstance().track("float_service", "悬浮客服");
                try {
                    // 打开客服
                    Intent intent = new Intent();
                    ComponentName cn = new ComponentName("com.jindashi.yingstock",
                            "com.jindashi.yingstock.business.customer.CustomerChatActivity");
                    intent.setComponent(cn);
                    mContext.startActivity(intent);

                    // 显示静图
                    Glide.with(mContext).load(R.drawable.icon_float_customer).into(mImageView);
                    mHasUnReadMsg = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 旋转图片
     */
    private void rotateImage(final float fromDegrees, final float toDegrees) {
        Animation mRotateAnimation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setDuration(100);
        mRotateAnimation.setRepeatCount(0);
        mRotateAnimation.setDetachWallpaper(true);
        mRotateAnimation.setInterpolator(new AccelerateInterpolator());
        mRotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsFold = toDegrees < 0;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(mRotateAnimation);
    }

    /**
     * 添加
     */
    public void add() {
        if (mWindowManager != null && !mIsAdded) {
            Glide.with(mContext).load(R.drawable.icon_float_customer).into(mImageView);
            mWindowManager.addView(mFloatView, mParams);
            mIsAdded = true;
        }

//        rotateImage(-70f, 0f);

        clearSubscription();
        addSubscription(RxBus.getInstance().register(UserEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserEvent>() {
                    @Override
                    public void accept(UserEvent event) throws Exception {
                        switch (event.getEventId()) {
                            case UserEvent.EVENT_NEW_MESSAGE:
                                // 显示动图
                                LogUtils.d(this, "有新的的消息-->");
                                Glide.with(mContext).load(R.drawable.icon_float_msg).into(mImageView);
                                mHasUnReadMsg = true;
//                                rotateImage(-70f, 0f);
                                break;
                        }
                    }
                }));

//        addFoldTimer();
    }

    /**
     * 展开
     */
    public void open() {
        if (mWindowManager != null && mIsAdded && mIsFold) {
            rotateImage(-70f, 0f);
        }
    }

    /**
     * 折叠
     */
    private void fold() {
        if (mWindowManager != null && mIsAdded && !mIsFold) {
            rotateImage(0f, -70f);
        }
    }

    /**
     * 移除
     */
    public void remove() {
        if (mWindowManager != null && mIsAdded) {
            mWindowManager.removeView(mFloatView);
            mIsAdded = false;
        }
        clearSubscription();
    }


    /**
     * 5秒后折叠图片定时任务
     */
    private void addFoldTimer() {
        mTimerDisposable = Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (!mHasUnReadMsg) {
                            fold();
                        }
                    }
                });
        addSubscription(mTimerDisposable);
    }

    /**
     * 触摸按下
     */
    public void touchDown() {
//        if (mIsShow) {
//            open();
//        }
    }

    /**
     * 触摸抬起
     */
    public void touchUp() {
//        if (!mIsShow)
//            return;
//        // 把之前可能未执行完的定时任务清空
//        removeSubscription(mTimerDisposable);
//        addFoldTimer();
    }

}
