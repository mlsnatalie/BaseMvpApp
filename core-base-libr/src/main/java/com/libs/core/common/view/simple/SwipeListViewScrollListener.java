package com.libs.core.common.view.simple;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;

/**
 * 解决SwipeRefreshLayout嵌套ListView引起的下拉事件冲突问题
 * 由于ListView与下拉刷新的滚动事件冲突, 使用这个ScrollListener可以避免ListView滑动异常
 */
public class SwipeListViewScrollListener implements AbsListView.OnScrollListener {

    private SwipeRefreshLayout mSwipeRefreshView;
    private AbsListView.OnScrollListener mOnScrollListener;

    public SwipeListViewScrollListener(SwipeRefreshLayout swipeView) {
        mSwipeRefreshView = swipeView;
    }

    public SwipeListViewScrollListener(SwipeRefreshLayout swipeView,
                                       AbsListView.OnScrollListener onScrollListener) {
        mSwipeRefreshView = swipeView;
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        View firstView = absListView.getChildAt(firstVisibleItem);
        // 当firstVisibleItem是第0位，如果firstView==null说明列表为空，需要刷新;
        // 如果top==0说明已经到达列表顶部,需要刷新
        if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
            mSwipeRefreshView.setEnabled(true);
        } else {
            mSwipeRefreshView.setEnabled(false);
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }
}
