package com.libs.core.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.Serializable;

/**
 * 原生暴露给H5的接口。
 * <p>
 * 方法可以传参也可以不传参。
 * 方法必须有返回值。
 *
 * @author zoubangyue
 */
public interface JSAppBridge extends Serializable {
    /*暴露给H5 的JS接口对象名称*/
    public static final String JS_OBJ_NAME = "jindashiapp";
    // 所有图片链接（已过滤）
    public static final String ALL_IMAGE_URLS = "all_image_urls";
    // 当前图片链接
    public static final String CURR_IMAGE_URL = "curr_image_url";


    @JavascriptInterface
    void openGroupChat(String masterId);

    @JavascriptInterface
    void openPrivacyProtocal();

    @JavascriptInterface
    public String init();

    /**
     * 例子：H5调用方式。
     * <p>
     * window.jindashi_jsappbrige.shareImageTextLink([JSONObject]);
     */
    @JavascriptInterface
    public String shareImageTextLink(String paramStr);

    @JavascriptInterface
    public void getUkey(String jsFun);

    @JavascriptInterface
    public String openWeb(String paramStr);

    @JavascriptInterface
    public String openLogin();

    @JavascriptInterface
    public String openStockSearch();

    @JavascriptInterface
    public void share(String params);

    @JavascriptInterface
    public void shareWXMiniProgram(String params, byte[] bytes);

    @JavascriptInterface
    public void openWeChatMiniProgram(String params);

    @JavascriptInterface
    public void shareData(String params);

    @JavascriptInterface
    public String openVideoLive();

    @JavascriptInterface
    public void openStockDetail(String params);

    @JavascriptInterface
    public void refreshSelfStock(String params);

    @JavascriptInterface
    public void openStockPool(String params);

    @JavascriptInterface
    public void openMoreHotNews(String params);

    @JavascriptInterface
    public void openPdf(String params);

    @JavascriptInterface
    public void openImage(String[] allUrls, String currUrl);


    @JavascriptInterface
    void onBack(String init);

    /**
     * 是否能关闭页面
     *
     * @return
     */
    boolean canClosePage();

    /**
     * 调用JS 返回方法
     *
     * @return
     */
    void invokeJsOnBack();


    /*登录返回*/
    public void onLoginRequest();

    public JSAppBridge setContext(Activity context);

    public JSAppBridge setWebView(WebView webView);


    public String getUserAgent();

    public String getParams();

    void showH5FontSettingView(); //展示h5的字体设置view

}
