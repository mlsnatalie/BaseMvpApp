package com.libs.core.common.adapter.recycler;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.libs.core.common.adapter.MViewHolder;

import java.util.List;

/**
 * RecyclerView多布局通用适配器
 *
 * @author zhang.zheng
 * @version 2018-05-23
 */
public abstract class RecyclerMultiAdapter<T> extends RecyclerBaseAdapter<T> {

    protected RecyclerMultiType<T> mRecyclerMultiType;

    public RecyclerMultiAdapter(Context context, List<T> dataList, RecyclerMultiType<T> recyclerMultiType) {
        super(context, -1, dataList);
        mRecyclerMultiType = recyclerMultiType;
        if (mRecyclerMultiType == null) {
            throw new IllegalArgumentException("The mRecyclerMultiType can’t be null.");
        }
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mRecyclerMultiType != null) {
            int layoutId = mRecyclerMultiType.getItemLayoutId(viewType);
            return MViewHolder.get(mContext, null, parent, layoutId, -1);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecyclerMultiType != null) {
            return mRecyclerMultiType.getItemViewType(position, mDataList.get(position));
        }
        return super.getItemViewType(position);
    }

}
