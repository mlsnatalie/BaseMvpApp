package com.libs.core.business.http;

/**
 * 应用所有的H5页面在此配置
 */
public class H5URL {

    // 诊股页
    public static final String WEB_DIAGNOSE = "/xigua/#/?stockid=%s&token=%s&uid=%s";
    // 免责声明
    public static final String WEB_DISCLAIMER = "/xigua/xieyi/disclaimer.html";
    // 权限开启说明
    public static final String WEB_LIMIT_INTRODUCE = "/xigua/xieyi/access.html";
    // 隐私声明
    public static final String WEB_PRIVACY = "/xigua/xieyi/privacyStatement.html";
    // 订阅声明
    public static final String WEB_SUBSCRIBE = "/xigua/xieyi/subscribeStatement.html";
    // 注册协议
    public static final String WEB_REGISTER = "/xigua/xieyi/regAgreement.html";
    // 服务协议
    public static final String WEB_SERVICE = "/xigua/xieyi/serve.html";
    // 有声早报
    public static final String WEB_SOUND_AUDIO = "/xigua/#/audio";
    // 每日收评
    public static final String WEB_EVERY_DAY_COMMENT = "/xigua/#/comment";
    //隐私政策协议
    public static final String WEB_PRIVACY_URL = "/xigua/#/PrivacyProtocal";
    //西瓜用户协议
    public static final String WEB_USER_PROTOCOL_URL = "/xigua/xieyi/userServiceProtocal.html";

    //风险提示协议
    public static final String WEB_RISK_URL = "/xigua/xieyi/warning.html";

    //首页h5
    public static final String WEB_WATERMELON_HOME = "/home/";

    //异动规则
    public static final String WEB_RULES = "/xigua/xieyi/rules.html";

    //华为渠道专门h5页面
    public static final String WEB_HW_HOME = "/xiguaTab/index.html";
    public static final String WEB_HW_MASTER = "/xiguaTab/master.html";
    public static final String WEB_HW_STOCK = "/xiguaTab/stock.html";
    public static final String WEB_HW_MARKET = "/xiguaTab/market.html";

    /**
     * 活动-相关web页
     */
    public static String getHDWebUrl(String action, Object... params) {
        String webUrl;
        if (params != null && params.length > 0) {
            webUrl = HttpURL.getHDBaseURL() + String.format(action, params);
        } else {
            webUrl = HttpURL.getHDBaseURL() + action;
        }
//        try {
//            webUrl = URLEncoder.encode(webUrl, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return webUrl;
    }

    public static String getStockFinance(String market, String code) {

        return "http://quotes.sina.cn/cn/view/finance_app_detail.php?symbol=" + market + code + "#/index";
    }

    public static String getStockF10(String market, String code) {
        return HttpURL.getHDBaseURL() + "/xigua/?id=" + market.toLowerCase() + code + "#/f10";
    }

    public static String getStockNewsUrl(String url) {
        return HttpURL.getHDBaseURL() + "/xigua/?url=" + url + "#/hangqinginformation";
    }


    public static String getMarketOverviewDetailUrl() {
        // return "https://hd.369wan.com/special/marketoverview/#/";
        return HttpURL.getH5BaseURL() + "/marketoverview/#/";
    }
}
