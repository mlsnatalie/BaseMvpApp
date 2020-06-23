package com.libs.core.common.view.smoothlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.libs.core.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SmoothListViewHeader extends LinearLayout {

    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private TextView mRefreshTime;
    private int mState = STATE_NORMAL;


    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public SmoothListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public SmoothListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) View.inflate(context, R.layout.smooth_header_view, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView) findViewById(R.id.smooth_header_arrow);
        mProgressBar = (ProgressBar) findViewById(R.id.smooth_header_progressbar);
        mHintTextView = (TextView) findViewById(R.id.smooth_header_refresh_hint);
        mRefreshTime = (TextView) findViewById(R.id.smooth_header_refresh_time);


        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(180);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(180);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) {
            // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
//            mLoadingImage.setVisibility(View.VISIBLE);
//            animationDrawable.start();
        } else {
            // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
//            mLoadingImage.setVisibility(View.INVISIBLE);
//            animationDrawable.stop();
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(R.string.smooth_header_pull_refresh);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.smooth_header_release_refresh);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.smooth_header_refreshing);
                break;
            default:
        }

        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    private String getRefreshTime(){
        return new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

}