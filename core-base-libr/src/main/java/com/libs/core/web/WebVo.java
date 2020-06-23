package com.libs.core.web;

import java.io.Serializable;

/**
 * Created by jince on 2018/1/29.
 */

public class WebVo implements Serializable {

    public static final String WEB_VO = "web_vo";

    private String url;
    private String title;
    private boolean hasTitle;
    private boolean hasShare;
    private JSAppBridge jsAppBridge;

    private boolean isQuotePDF;// 行情公告专用
    private boolean isCommonPDF;// 其它公用


    private String entrance;

    private String marker_code;

    public WebVo(String url, JSAppBridge jsAppBridge) {
        this.url = url;
        this.jsAppBridge = jsAppBridge;
    }

    public WebVo(String url, JSAppBridge jsAppBridge, boolean hasTitle) {
        this.url = url;
        this.hasTitle = hasTitle;
        this.jsAppBridge = jsAppBridge;
    }

    public WebVo(String url, JSAppBridge jsAppBridge, boolean hasTitle, boolean hasShare) {
        this.url = url;
        this.hasShare = hasShare;
        this.hasTitle = hasTitle;
        this.jsAppBridge = jsAppBridge;
    }

    public WebVo(String url, JSAppBridge jsAppBridge, boolean hasTitle, boolean hasShare, String entrance) {
        this.url = url;
        this.hasShare = hasShare;
        this.hasTitle = hasTitle;
        this.entrance = entrance;
        this.jsAppBridge = jsAppBridge;
    }

    public String getUrl() {
        return url;
    }

    public WebVo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public WebVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isHasTitle() {
        return hasTitle;
    }

    public WebVo setHasTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
        return this;
    }

    public boolean isHasShare() {
        return hasShare;
    }

    public WebVo setHasShare(boolean hasShare) {
        this.hasShare = hasShare;
        return this;
    }

    public JSAppBridge getJsAppBridge() {
        return jsAppBridge;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public boolean isQuotePDF() {
        return isQuotePDF;
    }

    public void setQuotePDF(boolean quotePDF) {
        isQuotePDF = quotePDF;
    }

    public String getMarker_code() {
        return marker_code;
    }

    public void setMarker_code(String marker_code) {
        this.marker_code = marker_code;
    }


    public boolean isCommonPDF() {
        return isCommonPDF;
    }

    public void setCommonPDF(boolean commonPDF) {
        isCommonPDF = commonPDF;
    }
}
