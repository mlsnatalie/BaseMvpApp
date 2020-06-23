package com.libs.core.common.adapter.recycler;

public interface RecyclerMultiType<T> {

    int getViewTypeCount();

    int getItemLayoutId(int itemType);

    int getItemViewType(int position, T bean);
}