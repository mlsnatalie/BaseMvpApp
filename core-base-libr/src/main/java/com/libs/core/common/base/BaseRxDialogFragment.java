package com.libs.core.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;

import com.libs.core.common.dialog.LoadingDialog;
import com.libs.core.common.utils.ToastHelper;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 *
 * @author zhang.zheng
 * @version 2018-05-16
 */
@SuppressWarnings("unchecked")
public abstract class BaseRxDialogFragment<P extends BasePresenter> extends DialogFragment implements BaseView {

    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected P mPresenter;
    protected View mRootView = null;

    protected Dialog mLoadingDialog;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        final Window window = getDialog().getWindow();
        mRootView = inflater.inflate(initLayout(), ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这2行,和上面的一样,注意顺序就行;
        mUnBinder = ButterKnife.bind(this, mRootView);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initViewData(savedInstanceState);

        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoading();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUnBinder.unbind();
    }


    @Override
    public void showLoading(String message) {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = LoadingDialog.createLoadingDialog(getActivity(), message);
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

    @Override
    public <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle.Event unEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, unEvent));
    }

    protected abstract int initLayout();

    protected abstract void initPresenter();

    protected abstract void initViewData(Bundle bundle);

    public void onRefresh(){}
}

