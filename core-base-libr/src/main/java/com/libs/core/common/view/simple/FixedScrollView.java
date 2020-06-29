package com.libs.core.common.view.simple;

import android.content.Context;
import android.graphics.Rect;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

public class FixedScrollView extends NestedScrollView {

    public FixedScrollView(Context context) {
        super(context);
    }

    public FixedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
