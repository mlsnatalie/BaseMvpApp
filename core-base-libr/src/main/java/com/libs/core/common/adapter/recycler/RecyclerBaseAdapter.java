package com.libs.core.common.adapter.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.libs.core.common.adapter.MViewHolder;

import java.util.List;

/**
 * RecyclerView通用适配器
 *
 * @author Zhang.Zheng
 * @version 2016-08-23
 */
public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<MViewHolder> {

    protected Context mContext;
    private int mLayoutId;
    protected List<T> mDataList;

    protected OnItemClickListener listener;


    public RecyclerBaseAdapter(Context context, int layoutId, List<T> dataList) {
        mContext = context;
        mLayoutId = layoutId;
        mDataList = dataList;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MViewHolder.get(mContext, null, parent, mLayoutId, -1);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, final int position) {
        holder.setNewPosition(position);
        if (position < mDataList.size()) {
            convert(holder, position, mDataList.get(position));
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(RecyclerBaseAdapter.this, position);
                    }
                }
            });
        } else {
            convert(holder, position, null);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && !mDataList.isEmpty()) {
            return mDataList.size();
        }
        return 0;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public T getItemAtPosition(int position) {
        if (mDataList != null && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    /**
     * 视图适配
     *
     * @param holder   视图
     * @param position 索引
     * @param itemVo   数据
     */
    public abstract void convert(MViewHolder holder, int position, T itemVo);

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(RecyclerBaseAdapter parent, int position);

    }

}
