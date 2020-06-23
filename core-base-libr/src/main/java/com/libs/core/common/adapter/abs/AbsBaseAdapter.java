package com.libs.core.common.adapter.abs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.libs.core.common.adapter.MViewHolder;

import java.util.List;

/**
 * 通用的适配器类
 * 适用于ListView、GridView、ExpandableListView数据适配
 *
 * @param <T>
 * @author zhang.zheng
 * @version 2018-05-23
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    protected int mLayoutId;
    protected Context mContext;
    protected List<T> mDataList;

    public AbsBaseAdapter(Context context, int layoutId, List<T> dataList) {
        mContext = context;
        mLayoutId = layoutId;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        if (mDataList != null && !mDataList.isEmpty()) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mDataList != null && !mDataList.isEmpty()) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MViewHolder holder = MViewHolder.get(mContext, convertView, parent, mLayoutId, position);
        convert(holder, position, getItem(position));
        return holder.getConvertView();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> mDataList) {
        this.mDataList = mDataList;
    }

    /**
     * 视图适配
     *
     * @param holder   视图
     * @param position 索引
     * @param itemVo   数据
     */
    public abstract void convert(MViewHolder holder, int position, T itemVo);

}
