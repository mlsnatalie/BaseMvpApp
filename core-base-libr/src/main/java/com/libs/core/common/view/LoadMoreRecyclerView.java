package com.libs.core.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.libs.core.common.adapter.recycler.listener.LoadMoreListener;

public class LoadMoreRecyclerView extends RecyclerView {

    private LinearLayoutManager mLinearLayoutManager;

    //当前页从0开始
//    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int preivousTotal = 0;

    //屏幕上可见的item数量
    private int visibleItemCount;

    //屏幕上可见的Item中的第一个
    private int fistVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    private LoadMoreListener listener;

    private boolean loadMoreEnable = true;

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            mLinearLayoutManager = (LinearLayoutManager) layout;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (loadMoreEnable && listener != null) {
            //屏幕可见的条目数量
            visibleItemCount = getChildCount();
            //已经加载出来的条目数量
            totalItemCount = mLinearLayoutManager.getItemCount();
            //屏幕第一个条目可见对应的位置
            fistVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            //如果是正在加载数据
            if (loading) {
                if (totalItemCount > preivousTotal) {
                    //说明数据已经加载结束
                    loading = false;
                    preivousTotal = totalItemCount;
                }
            }
            if (!loading && totalItemCount - visibleItemCount <= fistVisibleItem) {
//                currentPage++;
                listener.onLoadMore();
                loading = true;

            }
        }

    }

    public void resetLoading(boolean loading) {
        this.loading = loading;
    }

    public void loadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }
}
