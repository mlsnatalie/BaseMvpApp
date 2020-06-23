package com.libs.core.common.view;

import android.content.Context;
import android.util.AttributeSet;

import com.sunfusheng.marqueeview.MarqueeView;

/**
 * <pre>
 *     author : amos
 *     e-mail : hui.li1@yintech.cn
 *     time   : 2019/09/10 19:45
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class XGMarqueeView<T> extends MarqueeView<T> {
    public XGMarqueeView(Context context) {
        super(context);
    }

    public XGMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception e) {

        }
    }
}
