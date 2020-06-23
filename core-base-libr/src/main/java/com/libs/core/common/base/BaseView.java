package com.libs.core.common.base;

import android.arch.lifecycle.Lifecycle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.libs.core.common.utils.LogUtils;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * View层公共方法
 *
 * @author zhang.zheng
 * @version 2017-12-16
 */
public interface BaseView {

    void showToast(String message);

    void showLoading(String message);

    void hideLoading();

    <T> AutoDisposeConverter<T> bindLifecycle();

    <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle.Event event);

    default void onRefresh(){}

    default void subOn() {
        LogUtils.d("BaseView", "subOn");
    }

    default void subOff() {
        LogUtils.d("BaseView", "subOff");
    }

    /**
     * ViewPager 管理Fragment 生命周期控制
     *
     * @param hidden
     * @param viewPager
     * @param fragmentStatePagerAdapter
     */
    default void onHiddenChanged(boolean hidden, ViewPager viewPager, FragmentStatePagerAdapter fragmentStatePagerAdapter) {
        if (viewPager != null && fragmentStatePagerAdapter != null) {
            Fragment fragment = fragmentStatePagerAdapter.getItem(viewPager.getCurrentItem());
            if (fragment != null && fragment instanceof BaseView) {
                if (hidden)
                    ((BaseView) fragment).subOff();
                else
                    ((BaseView) fragment).subOn();
            }
        }
    }

    /**
     * ViewPager 管理Fragment 生命周期控制
     *
     * @param hidden
     * @param viewPager
     * @param fragmentStatePagerAdapter
     */
    default void onHiddenChanged(boolean hidden, ViewPager viewPager, FragmentPagerAdapter fragmentStatePagerAdapter) {
        if (viewPager != null && fragmentStatePagerAdapter != null) {
            Fragment fragment = fragmentStatePagerAdapter.getItem(viewPager.getCurrentItem());
            if (fragment != null && fragment instanceof BaseView) {
                if (hidden)
                    ((BaseView) fragment).subOff();
                else
                    ((BaseView) fragment).subOn();
            }
        }
    }
}
