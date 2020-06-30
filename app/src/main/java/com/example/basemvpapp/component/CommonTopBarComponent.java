package com.example.basemvpapp.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.basemvpapp.R;
import com.libs.core.common.helper.StatusBarCompatHelper;

/**
 * @author: amos
 * @date: 2019/12/18 10:32
 * @description: 通用的顶部栏
 */
public class CommonTopBarComponent extends LinearLayout implements CommonTopBarContract {
    public CommonTopBarComponent(Context context) {
        this(context, null);
    }

    public CommonTopBarComponent(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTopBarComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAttrs(attrs);
        init();
    }

    private View view_status_bar;
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_other;
    private View view_divider;
    private Context mContext;

    private String mBackWord;
    private int mBackWordColor;
    private float mBackWordSize;
    private int mBackIconRes;
    private String mTitleWord;
    private int mTitleWordColor;
    private float mTitleWordSize;
    private String mRightWord;
    private int mRightWordColor;
    private float mRightWordSize;
    private int mRightIcon;
    private int mBackGroundColor;

    private OnCallBack mCallBack;

    private void getAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CommonTopBarComponent);
        if (array == null) {
            return;
        }
        mBackWord = array.getString(R.styleable.CommonTopBarComponent_tb_back_word);
        mBackWordColor = array.getColor(R.styleable.CommonTopBarComponent_tb_back_word_color, Color.parseColor("#333333"));
        mBackWordSize = array.getDimension(R.styleable.CommonTopBarComponent_tb_back_word_size, spToPx(14));
        mBackIconRes = array.getResourceId(R.styleable.CommonTopBarComponent_tb_back_icon, R.drawable.ic_title_back2);
        mTitleWord = array.getString(R.styleable.CommonTopBarComponent_tb_title_word);
        mTitleWordColor = array.getColor(R.styleable.CommonTopBarComponent_tb_title_word_color, Color.parseColor("#333333"));
        mTitleWordSize = array.getDimension(R.styleable.CommonTopBarComponent_tb_title_word_size, spToPx(18));
        mRightWord = array.getString(R.styleable.CommonTopBarComponent_tb_right_word);
        mRightWordColor = array.getColor(R.styleable.CommonTopBarComponent_tb_right_word_color, Color.parseColor("#333333"));
        mRightWordSize = array.getDimension(R.styleable.CommonTopBarComponent_tb_right_word_size, spToPx(14));
        mRightIcon = array.getResourceId(R.styleable.CommonTopBarComponent_tb_right_icon, 0);
        mBackGroundColor = array.getColor(R.styleable.CommonTopBarComponent_tb_background_color, ContextCompat.getColor(mContext, R.color.white/*android.R.color.transparent*/));

        array.recycle();
    }

    private void init() {
        this.setClickable(true);
        //this.setBackgroundColor(Color.parseColor("#ffffff"));
        this.setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(mContext).inflate(R.layout.layout_common_topbar_component, this, true);
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private void setDefaultConfig() {
        setBackWord(mBackWord);
        setBackWordColor(mBackWordColor);
        tv_back.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBackWordSize);
        setBackIcon(mBackIconRes);
        setTitleWord(mTitleWord);
        setTitleWordColor(mTitleWordColor);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleWordSize);
        setRightWord(mRightWord);
        setRightWordColor(mRightWordColor);
        tv_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightWordSize);
        setRightIcon(mRightIcon);
        setBackGroundColor(mBackGroundColor);
        //setBackGroupAlpha(0f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view_status_bar = findViewById(R.id.view_status_bar);
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener((view) -> {
            if (mCallBack != null) {
                mCallBack.onBackClick();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_other = findViewById(R.id.tv_other);
        tv_other.setOnClickListener((view -> {
            if (mCallBack != null) {
                mCallBack.onOtherClick();
            }
        }));
        view_divider = findViewById(R.id.view_divider);
        setDefaultConfig();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarCompatHelper.getStatusBarHeight((Activity) mContext, new StatusBarCompatHelper.OnStatusBarHeightCallBack() {
                @Override
                public void setHeight(int statusBarHeight) {
                    LayoutParams params = (LayoutParams) view_status_bar.getLayoutParams();
                    params.height = statusBarHeight;
                    view_status_bar.setLayoutParams(params);
                }
            });
        }
    }

    @Override
    public CommonTopBarComponent setBackIcon(int idRes) {
        try {
            Drawable drawable = getResources().getDrawable(idRes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_back.setCompoundDrawables(drawable, null, null, null);
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public CommonTopBarComponent setHideBackIcon() {
        tv_back.setCompoundDrawables(null, null, null, null);
        return this;
    }


    @Override
    public CommonTopBarComponent setBackWord(String backWord) {
        tv_back.setText(TextUtils.isEmpty(backWord) ? "" : backWord);
        return this;
    }

    @Override
    public CommonTopBarComponent setBackWordColor(int colorInt) {
        tv_back.setTextColor(colorInt);
        return this;
    }

    @Override
    public CommonTopBarComponent setBackWordSize(int sp) {
        tv_back.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    @Override
    public CommonTopBarComponent setTitleWord(String title) {
        tv_title.setText(TextUtils.isEmpty(title) ? "" : title);
        return this;
    }

    @Override
    public CommonTopBarComponent setTitleWordColor(int colorInt) {
        tv_title.setTextColor(colorInt);
        return this;
    }

    @Override
    public CommonTopBarComponent setTitleWordSize(int sp) {
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    @Override
    public CommonTopBarComponent setRightWord(String rightWord) {
        tv_other.setText(TextUtils.isEmpty(rightWord) ? "" : rightWord);
        return this;
    }

    @Override
    public CommonTopBarComponent setRightWordColor(int colorInt) {
        tv_other.setTextColor(colorInt);
        return this;
    }

    @Override
    public CommonTopBarComponent setRightWordSize(int sp) {
        tv_other.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    @Override
    public CommonTopBarComponent setRightIcon(int idRes) {
        try {
            if(idRes == 0){
                return this;
            }
            Drawable drawable = getResources().getDrawable(idRes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_other.setCompoundDrawables(null, null, drawable, null);
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public CommonTopBarComponent setEnableDividerView(boolean isShow) {
        view_divider.setVisibility(isShow ? VISIBLE : GONE);
        return this;
    }

    @Override
    public CommonTopBarComponent setEnableStatusView(boolean isShow) {
        view_status_bar.setVisibility(isShow ? VISIBLE : GONE);
        return this;
    }

    @Override
    public CommonTopBarComponent setBackGroundColor(int colorInt) {
        try {
            this.setBackgroundColor(colorInt);
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public CommonTopBarComponent setBackGroundAlpha(float alpha) {
        try {
            if (alpha < 0f) {
                alpha = 0f;
            } else if (alpha > 1f) {
                alpha = 1f;
            }
            getBackground().mutate().setAlpha((int) (alpha * 255));
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public CommonTopBarComponent setTitleAlpha(float alpha) {
        try {
            if (alpha < 0f) {
                alpha = 0f;
            } else if (alpha > 1f) {
                alpha = 1f;
            }
            tv_title.setAlpha(alpha);
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public CommonTopBarComponent setRightAlpha(float alpha) {
        try {
            if (alpha < 0f) {
                alpha = 0f;
            } else if (alpha > 1f) {
                alpha = 1f;
            }
            tv_other.setAlpha(alpha);
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public TextView getBackView() {
        return tv_back;
    }

    @Override
    public TextView getTitleView() {
        return tv_title;
    }

    @Override
    public TextView getRightView() {
        return tv_other;
    }

    @Override
    public View getStatusView() {
        return view_status_bar;
    }

    @Override
    public View getDividerView() {
        return view_divider;
    }

    @Override
    public CommonTopBarComponent setStatusViewColor(int color) {
        if (view_status_bar != null) {
            view_status_bar.setBackgroundColor(color);
        }
        return this;
    }

    @Override
    public CommonTopBarComponent setCallBack(OnCallBack callBack) {
        mCallBack = callBack;
        return this;
    }
}
