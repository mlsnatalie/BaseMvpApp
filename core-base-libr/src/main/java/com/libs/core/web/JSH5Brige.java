package com.libs.core.web;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * H5暴露给原生的接口。
 *
 * @author zoubangyue
 */
public interface JSH5Brige {
    public WebView getWebView();

    @JavascriptInterface
    public void jsInit();

    @JavascriptInterface
    public void share();
}
