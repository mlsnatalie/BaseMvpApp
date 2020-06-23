package com.libs.core.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.libs.core.common.dialog.LoadingDialog;
import com.libs.core.common.permission.PermissionHelper;
import com.libs.core.common.utils.LogUtils;
import com.libs.core.common.utils.ToastHelper;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.autosize.AutoSizeCompat;

/**
 * Activity基类
 *
 * @author zhang.zheng
 * @version 2018-05-10
 */
@SuppressWarnings("unchecked")
public abstract class BaseRxActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected String TAG = getClass().getSimpleName();
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected P mPresenter;

    protected Dialog mLoadingDialog;

    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TAG = getClass().getSimpleName();
        TAG = getTAG();
        LogUtils.d(TAG, "onCreate");
        setContentView(initLayout());
        mContext = this;
        mUnBinder = ButterKnife.bind(this);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.setTAG(TAG);
        }

        initViewData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        hideLoading();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUnBinder.unbind();
        super.onDestroy();
    }


    @Override
    public void showLoading(String message) {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = LoadingDialog.createLoadingDialog(this, message);
            }
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String message) {
        ToastHelper.getInstance().showCenter(mContext, message);
    }

    @Override
    public <T> AutoDisposeConverter<T> bindLifecycle() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
    }

    public <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, untilEvent));
    }


    protected abstract int initLayout();

    protected abstract void initPresenter();

    protected abstract void initViewData(Bundle bundle);

    public void onRefresh() {
    }

    public String getTAG(){

        return getClass().getSimpleName();
    }
    /**
     * 设置应用全局字体的默认配置，避免受系统字体大小设置影响导致界面错乱。
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null && res.getConfiguration().fontScale != 1) {//非默认值
            LogUtils.d(TAG, "getResources");
            Configuration config = res.getConfiguration();
            res.getConfiguration().fontScale = 1;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        //处理当前页面 适配出错问题
        if (isSupportAutoSize() && Looper.myLooper() == Looper.getMainLooper()) {
            AutoSizeCompat.autoConvertDensityOfGlobal(res);
        }
        return res;
    }

    protected boolean isSupportAutoSize() {
        return true;
    }

    public PermissionHelper getPermissionHelper() {
        if (mPermissionHelper == null)
            mPermissionHelper = new PermissionHelper(this);
        return mPermissionHelper;
    }

    @Override
    public void subOn() {
        if (mPresenter != null)
            mPresenter.subOn();
    }

    @Override
    public void subOff() {
        if (mPresenter != null)
            mPresenter.subOff();
    }
}
