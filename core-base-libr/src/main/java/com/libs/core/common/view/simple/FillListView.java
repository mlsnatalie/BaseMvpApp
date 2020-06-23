package com.libs.core.common.view.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class FillListView extends ListView {

    public FillListView(Context context) {
        super(context);
    }

    public FillListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写该方法，解决ScrollView嵌套ListView只显示一条数据的问题
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
