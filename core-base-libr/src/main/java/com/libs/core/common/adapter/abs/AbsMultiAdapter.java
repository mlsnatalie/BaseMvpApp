package com.libs.core.common.adapter.abs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.libs.core.common.adapter.MViewHolder;

import java.util.List;

/**
 * 通用多布局通用适配器
 * 适用于ListView、GridView、ExpandableListView数据适配
 *
 * @author zhang.zheng
 * @version 2018-05-23
 */
public abstract class AbsMultiAdapter<T> extends AbsBaseAdapter<T> {

    private AbsMultiType<T> mAbsMultiType;

    public AbsMultiAdapter(Context context, List<T> dataList, AbsMultiType<T> absMultiType) {
        super(context, -1, dataList);
        mAbsMultiType = absMultiType;
        if (mAbsMultiType == null) {
            throw new IllegalArgumentException("The mAbsMultiType can't be null.");
        }
    }

    @Override
    public int getViewTypeCount() {
        if (mAbsMultiType != null) {
            return mAbsMultiType.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mAbsMultiType != null) {
            return mAbsMultiType.getItemViewType(position, mDataList.get(position));
        }
        return super.getItemViewType(position);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mAbsMultiType != null) {
            int layoutId = mAbsMultiType.getItemLayoutId(position, getItem(position));
            MViewHolder viewHolder = MViewHolder.get(mContext, convertView, parent, layoutId, position);
            convert(viewHolder, position, getItem(position));
            return viewHolder.getConvertView();
        }
        return super.getView(position, convertView, parent);
    }

}
