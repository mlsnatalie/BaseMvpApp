package com.example.basemvpapp.component;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * @author: amos
 * @date: 2019/12/18 12:46
 * @description:
 */
public interface CommonTopBarContract {
    CommonTopBarComponent setBackIcon(@DrawableRes int idRes);

    CommonTopBarComponent setHideBackIcon();

    CommonTopBarComponent setBackWord(String backWord);

    CommonTopBarComponent setBackWordColor(@ColorInt int colorInt);

    CommonTopBarComponent setBackWordSize(int sp);

    CommonTopBarComponent setTitleWord(String title);

    CommonTopBarComponent setTitleWordColor(@ColorInt int colorInt);

    CommonTopBarComponent setTitleWordSize(int sp);

    CommonTopBarComponent setRightWord(String rightWord);

    CommonTopBarComponent setRightWordColor(@ColorInt int colorInt);

    CommonTopBarComponent setRightWordSize(int sp);

    CommonTopBarComponent setRightIcon(@DrawableRes int idRes);

    CommonTopBarComponent setEnableDividerView(boolean isShow);

    CommonTopBarComponent setEnableStatusView(boolean isShow);

    CommonTopBarComponent setBackGroundColor(@ColorInt int colorInt);

    CommonTopBarComponent setBackGroundAlpha(float alpha);

    CommonTopBarComponent setTitleAlpha(float alpha);

    CommonTopBarComponent setRightAlpha(float alpha);

    TextView getBackView();

    TextView getTitleView();

    TextView getRightView();

    View getStatusView();

    View getDividerView();

    CommonTopBarComponent setStatusViewColor(int color);

    CommonTopBarComponent setCallBack(OnCallBack callBack);

    abstract class OnCallBack {
        public void onBackClick() {

        }

        public void onOtherClick() {

        }
    }
}
