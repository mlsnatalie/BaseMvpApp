package com.libs.core.common.adapter.abs;

public interface AbsMultiType<T> {

    int getViewTypeCount();

    int getItemLayoutId(int position, T bean);

    int getItemViewType(int position, T bean);
}