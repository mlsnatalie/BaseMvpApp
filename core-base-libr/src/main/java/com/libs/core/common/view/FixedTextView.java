package com.libs.core.common.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.libs.core.common.utils.LogUtils;

/**
 * Created by zzm on 2018/9/4.
 */

public class FixedTextView extends AppCompatTextView {

    public FixedTextView(Context context) {
        super(context);
    }

    public FixedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        float textheight = fontMetrics.bottom - fontMetrics.top;
        float linespace = getLineSpacingExtra();
        int maxLine = (int) Math.ceil(h / (getLineHeight() + linespace));
        setMaxLines(maxLine);
        setEllipsize(TextUtils.TruncateAt.END);
        setText(getText());
        LogUtils.d("FixedTextView", "FixedTextView height = " + h + ", maxLine = " + maxLine + ", textheight = " + textheight);
    }
}
