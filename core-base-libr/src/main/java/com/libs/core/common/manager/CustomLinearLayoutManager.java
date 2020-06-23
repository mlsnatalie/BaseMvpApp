package com.libs.core.common.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    /**
     * 禁止滑动
     * canScrollHorizontally（禁止横向滑动）
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollVertically();
    }
    /**
     * 禁止滑动
     * canScrollVertically（禁止竖向滑动）
     * @return
     */
    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }


}
