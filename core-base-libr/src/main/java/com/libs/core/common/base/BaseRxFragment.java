package com.libs.core.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.libs.core.common.dialog.LoadingDialog;
import com.libs.core.common.permission.PermissionHelper;
import com.libs.core.common.utils.LogUtils;
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
 * https://blog.csdn.net/q564063517/article/details/79381113
 */
@SuppressWarnings("unchecked")
public abstract class BaseRxFragment<P extends BasePresenter> extends Fragment implements BaseView {

    protected String TAG = getClass().getSimpleName();
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected P mPresenter;
    protected View mRootView = null;

    protected Dialog mLoadingDialog;

    protected PermissionHelper mPermissionHelper;

    protected boolean isCanShowing = true;

    public void setTAG(Object tag) {
        // 外部加索引
        this.TAG += tag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
        LogUtils.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView");
        mRootView = inflater.inflate(initLayout(), container, false);
        //mRootView.setFocusable(true);
        //mRootView.setFocusableInTouchMode(true);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(TAG, "onViewCreated");
        mUnBinder = ButterKnife.bind(this, mRootView);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.setTAG(TAG);
        }
        initView(savedInstanceState);
        initData();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LogUtils.d(TAG, "onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d(TAG, "onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        isCanShowing = isVisibled();
        LogUtils.d(TAG, "onResume");
        subOn();
    }

    @Override
    public void onPause() {
        super.onPause();
        isCanShowing = false;
        LogUtils.d(TAG, "onPause");
        subOff();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 使用场景hide show，新创建的不会调用onHiddenChanged方法
        isCanShowing = !hidden;
        LogUtils.d(TAG, "onHiddenChanged ， hidden = " + hidden);
        onVisibleChanged(isVisibleOnScreen());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "setUserVisibleHint ， isVisibleToUser = " + isVisibleToUser);
        // 使用场景 FragmentPagerAdapter+ViewPager
        // oncreateView 之前调用，注意不要使用控件，所以他有可能在onResume之前调用
        isCanShowing = isVisibleToUser;
        onVisibleChanged(isVisibleOnScreen());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
        hideLoading();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUnBinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d(TAG, "onDetach");
    }

    @Override
    public void showLoading(String message) {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = LoadingDialog.createLoadingDialog(mContext, message);
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
    public <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, untilEvent));
    }

    protected abstract int initLayout();

    protected abstract void initPresenter();

    protected abstract void initView(Bundle bundle);

    protected abstract void initData();

//    /**
//     * 找到activity的控件
//     *
//     * @param <T> the type parameter
//     * @param id  the id
//     * @return the t
//     */
//    @SuppressWarnings("unchecked")
//    protected <T extends View> T findActivityViewById(@IdRes int id) {
//        return (T) mContext.findViewById(id);
//    }

    public PermissionHelper getPermissionHelper() {
        if (mPermissionHelper == null)
            mPermissionHelper = new PermissionHelper(this.getActivity());
        return mPermissionHelper;
    }

    @Override
    public void subOn() {
        if (mPresenter != null && isVisibleOnScreen())
            mPresenter.subOn();
    }

    @Override
    public void subOff() {
        if (mPresenter != null)
            mPresenter.subOff();
    }

    // 判断可见性，对手动显示与PagerAdapter方式均有效，且跟随父fragment可见性状态
    public boolean isVisibleOnScreen() {
        if (isCanShowing && getUserVisibleHint() && isVisibled()) {
            if (getParentFragment() == null) {
                // 根Fragment
                return true;
            }

            if (getParentFragment() instanceof BaseRxFragment) {
                return ((BaseRxFragment) getParentFragment()).isVisibleOnScreen();
            } else {
                return getParentFragment().isVisible();
            }
        }
        return false;
    }

    protected void onVisibleChanged(boolean isVisible) {
        if (mPresenter != null) {
            if (isVisible) {
                mPresenter.subOn();
            } else
                mPresenter.subOff();
        }
    }

    public boolean isVisibled() {
        return isAdded() && !isHidden() && getView() != null && getView().getVisibility() == View.VISIBLE;
    }
}

