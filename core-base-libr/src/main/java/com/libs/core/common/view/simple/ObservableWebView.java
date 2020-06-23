package com.libs.core.common.view.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by blazers on 2017/8/9.
 */

public class ObservableWebView extends WebView {

    public static final int SCROLL_UP = 10;
    public static final int SCROLL_DOWN = 20;

    private int mCurrentScrollDirection = SCROLL_UP;

    private OnScrollToEdgeListener mOnScrollToEdgeListener;

    public ObservableWebView(Context context) {
        super(context);
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t - oldt >= 0) {
            mCurrentScrollDirection = SCROLL_DOWN;
        } else {
            mCurrentScrollDirection = SCROLL_UP;
        }
        if (mOnScrollToEdgeListener != null) {
            mOnScrollToEdgeListener.onScrolling();
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (clampedY) {
            if (mOnScrollToEdgeListener != null) {
                mOnScrollToEdgeListener.onScrollToEdge(mCurrentScrollDirection);
            }
        }
    }

    public void setOnScrollToEdgeListener(OnScrollToEdgeListener onScrollToEdgeListener) {
        mOnScrollToEdgeListener = onScrollToEdgeListener;
    }

    public interface OnScrollToEdgeListener {
        void onScrollToEdge(int scrollDirection);
        void onScrolling();
        void onCannotScroll();
    }

    public static class ObservableWebViewClient extends WebViewClient {

        private ObservableWebView mObservableWebView;

        public ObservableWebViewClient(ObservableWebView observableWebView) {
            mObservableWebView = observableWebView;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("====", "Url: " + url + "   [FINISHED]");
            view.post(new Runnable() {
                @Override
                public void run() {
                    // 尝试获取高度
                    mObservableWebView.measure(0, 0);
                    int contentHeight = mObservableWebView.getMeasuredHeight();
                    Log.i("====", "Content height: " + contentHeight + "   WebView Height: " + mObservableWebView.getHeight());
                    if (contentHeight <= mObservableWebView.getHeight()) {
                        if (mObservableWebView.mOnScrollToEdgeListener != null) {
                            mObservableWebView.mOnScrollToEdgeListener.onCannotScroll();
                        }
                    }
                }
            });
        }
    }
}
