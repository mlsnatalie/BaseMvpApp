package com.libs.core.common.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * 设置View的按压透明效果
 */
public class SetViewAlphaListener implements View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            v.setAlpha(0.4F);
        else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_CANCEL)
            v.setAlpha(1);

        return false;
    }
}
