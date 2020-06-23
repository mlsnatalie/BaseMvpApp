package com.libs.core.common.adapter.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.libs.core.R;
import com.libs.core.common.adapter.MViewHolder;

import java.util.List;

/**
 * RecyclerView通用适配器
 *
 * @author Zhang.Zheng
 * @version 2016-08-23
 */
public abstract class RecyclerBaseMoreAdapter<T> extends RecyclerBaseAdapter<T> {

    public static final int FOOTER_TYPE = 0xff;

    private boolean enableLoadMore = true;

    public RecyclerBaseMoreAdapter(Context context, int layoutId, List<T> dataList) {
        super(context, layoutId, dataList);
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE) {
            return MViewHolder.get(mContext, null, parent, R.layout.item_footer_load_more, -1);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList != null && !mDataList.isEmpty() && position == mDataList.size()) {
            return FOOTER_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && !mDataList.isEmpty()) {
            return super.getItemCount() + (enableLoadMore ? 1 : 0);
        }
        return super.getItemCount();
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
    }

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }
}
