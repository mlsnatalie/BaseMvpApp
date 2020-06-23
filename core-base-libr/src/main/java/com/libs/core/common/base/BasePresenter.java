package com.libs.core.common.base;

import android.content.Context;

/**
 * 控制器基类
 *
 * @author zhang.zheng
 * @version 2018-01-08
 */
public abstract class BasePresenter<V extends BaseView> {

    protected String TAG = getClass().getSimpleName();
    protected V mView;
    protected Context mContext;

    public BasePresenter(Context context) {
        this.mContext = context;
    }

    protected void attachView(V view) {
        this.mView = view;
    }

    protected void detachView() {
        this.mView = null;
        this.mContext = null;
    }

    public void subOn() {

    }

    public void subOff() {

    }

    public void setTAG(Object TAG) {
        this.TAG = "" + TAG;
    }
}
