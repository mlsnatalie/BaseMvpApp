package com.libs.core.business.http;


import com.libs.core.business.AppConfig;

import retrofit2.http.PUT;

/**
 * 全局HTTP域名配置类
 *
 * @author zhang.zheng
 * @version 2018-06-14
 */
public class HttpURL {

    private static boolean DEBUG = AppConfig.DEBUG;

    private static String appName = "ygzt";

    private static String appCate = "1";

    private static String appType = "1";

    /**
     * CAS
     */
    private static final String TEST_CAS_BASE_URL = "http://test-cas-center.jindashi.cn:8130";
    private static final String RELEASE_CAS_BASE_URL = "https://cas.369wan.com";

    /**
     * CMS
     */
    private static final String TEST_CMS_BASE_URL = "https://test-cms.369wan.com";
    private static final String RELEASE_CMS_BASE_URL = "https://cms.369wan.com";

    /**
     * CMS
     */
    private static final String TEST_CMS_JDS_BASE_URL = "https://test-cms.jindashi.cn";
    private static final String RELEASE_CMS_JDS_BASE_URL = "https://cms.jindashi.cn";

    /**
     * CRM
     */
    private static final String TEST_CRM_BASE_URL = "http://test-crm.369wan.com";
    private static final String RELEASE_CRM_BASE_URL = "https://crm.369wan.com";

    /**
     * 选股
     */
    private static final String TEST_STOCK_BASE_URL = "http://test-aigw.369wan.com";
    private static final String RELEASE_STOCK_BASE_URL = "https://aigw.369wan.com";

    /**
     * 新浪接口
     */
    private static final String RELEASE_SINA_STOCK_BASE_URL = "https://touzi.sina.com.cn";

    /**
     * 活动（Web）
     */
    private static final String TEST_HD_BASE_URL = "https://hd.369wan.com/xiguatest";
    private static final String RELEASE_HD_BASE_URL = "https://hd.369wan.com/special";

    /**
     * 消息中心
     */
    private static final String TEST_MESSAGE_URL = "http://test-cas-center.jindashi.cn:8130";
    private static final String RELEASE_MESSAGE_URL = "https://apigateway.jindashi.cn";

    /**
     * 消息推送
     */
    private static final String TEST_MSG_PUSH_URL = "https://test-pushmsg.jindashi.cn";
    private static final String RELEASE_MSG_PUSH_URL = "https://pushmsg.jindashi.cn";

    /**
     * 直播室
     */
    private static final String TEST_LIVE_URL = "https://test-apijmzb.jindashi.cn";
    private static final String RELEASE_LIVE_URL = "https://apijmzb.jindashi.cn";
    /**
     * 选读精华
     */
    private static final String TEST_JDS_URL = "http://test.jds.jindashi.cn";
    private static final String RELEASE_JDS_URL = "http://jds.jindashi.cn";

    /**
     * 集团趋势观股
     **/
    private static final String TEST_QUOTE_FDZQ_URL = "http://test-quotes.fdzq.com:8643";
    private static final String RELEASE_QUOTE_FDZQ_URL = "http://valueaddinfo.fdzq.com:80";

    private static final String TEST_LIVE_URL_NEW = "http://test-apigateway-v2.jindashi.cn";
    private static final String RELEASE_LIVE_URL_NEW = "https://apigateway-v2.jindashi.cn";

    public static final String SHARE_APP = "http://www.369wan.com/mdownload.html";

    /**
     * 股票详情页 个股关联板块
     */
    public static final String TEST_INDIVIDUAL_STOCK_URL = "http://test-quotes.fdzq.com:8645";
    public static final String RELEASE_INDIVIDUAL_STOCK_URL = "http://baseinfo.fdzq.com:8080";

    /**
     * 行情页 资金净流入
     */
    public static final String TEST_QUOTE_FUNDS = "http://test-quotes.fdzq.com:8073";
    public static final String RELEASE_QUOTE_FUNDS = "http://valueaddinfo.fdzq.com";

    /**
     * 市场总览
     */
    public static final String TEST_MARKET_OVERVIEW_URL = "http://test-quotes.fdzq.com:8073";
    public static final String RELEASE_MARKET_OVERVIEW_URL = "https://valadd.licaishisina.com";


    public static final String TEST_API_JDS = "https://test-apigateway.jindashi.cn";
    public static final String API_JDS = "https://apigateway.jindashi.cn";


    public static String getCasBaseURL() {
        return DEBUG ? TEST_CAS_BASE_URL : RELEASE_CAS_BASE_URL;
    }

    public static String getCmsBaseURL() {
        return DEBUG ? TEST_CMS_BASE_URL : RELEASE_CMS_BASE_URL;
    }

    public static String getCmsJDSBaseURL() {
        return DEBUG ? TEST_CMS_JDS_BASE_URL : RELEASE_CMS_JDS_BASE_URL;
    }

    public static String getCrmBaseURL() {
        return DEBUG ? TEST_CRM_BASE_URL : RELEASE_CRM_BASE_URL;
    }

    public static String getSinaStockBaseURL() {
        return DEBUG ? RELEASE_SINA_STOCK_BASE_URL : RELEASE_SINA_STOCK_BASE_URL;
    }

    public static String getCrmActionURL(String action) {
        return (DEBUG ? TEST_CRM_BASE_URL : RELEASE_CRM_BASE_URL) + action;
    }

    public static String getMessageBaseURL() {
        return DEBUG ? TEST_MESSAGE_URL : RELEASE_MESSAGE_URL;
    }

    public static String getMessageActionURL(String action) {
        return (DEBUG ? TEST_MESSAGE_URL : RELEASE_MESSAGE_URL) + action;
    }

    public static String getStockBaseURL() {
        return DEBUG ? TEST_STOCK_BASE_URL : RELEASE_STOCK_BASE_URL;
    }

    public static String getHDBaseURL() {
        return DEBUG ? TEST_HD_BASE_URL : RELEASE_HD_BASE_URL;
    }

    public static String getJDSURL() {
        return DEBUG ? TEST_JDS_URL : RELEASE_JDS_URL;
    }

    public static String getMsgPushBaseURL() {
        return DEBUG ? TEST_MSG_PUSH_URL : RELEASE_MSG_PUSH_URL;
    }

    public static String getLiveBaseURL() {
        return DEBUG ? TEST_LIVE_URL : RELEASE_LIVE_URL;
    }

    public static String getLiveBaseURLNEW() {
        return DEBUG ? TEST_LIVE_URL_NEW : RELEASE_LIVE_URL_NEW;
    }

    public static String getQuoteFdzqUrl() {
        return DEBUG ? TEST_QUOTE_FDZQ_URL : RELEASE_QUOTE_FDZQ_URL;
    }

    /**
     * 股票详情页 个股关联
     */
    public static String getIndividualStockPlateBaseUrl() {
        return DEBUG ? TEST_INDIVIDUAL_STOCK_URL : RELEASE_INDIVIDUAL_STOCK_URL;
    }

    /**
     * 行情页 资金净流入
     *
     * @return
     */
    public static String getQuoteFundsBaseUrl() {
        return DEBUG ? TEST_QUOTE_FUNDS : RELEASE_QUOTE_FUNDS;
    }

    /**
     * 市场总览
     *
     * @return
     */
    public static String getMarketOverviewBaseUrl() {
        return DEBUG ? TEST_MARKET_OVERVIEW_URL : RELEASE_MARKET_OVERVIEW_URL;
    }

    /**
     * h5通用测试和正式地址
     *
     * @return
     */
    public static String getH5BaseURL() {
        return DEBUG ? "https://hd.369wan.com/xiguatest" : "https://hd.369wan.com/special";
    }

    /**
     * MD5秘钥
     */
    private static final String TEST_MD5_SECRET = "vkn8vG226ZDLp1W3ZBXD";
    private static final String RELEASE_MD5_SECRET = "PQkbGrPb5p2rGeLUiBDU";
    /**
     * MD5秘钥
     */
    private static final String TEST_MESSAGE_MD5_SECRET = "XGZT_APP";
    private static final String RELEASE_MESSAGE_MD5_SECRET = "thKuNOQyw7M5mXJDdqVH";

    /**
     * 新直播室接口密钥，测试环境是小写，正式环境为大写
     */
    private static final String LIVE_MD5_SECRET = "jindashiliveapi2019@";

    public static String getMD5Secret() {
        return DEBUG ? TEST_MD5_SECRET : RELEASE_MD5_SECRET;
    }

    public static String getMessageMD5Secret() {
        return DEBUG ? TEST_MESSAGE_MD5_SECRET : RELEASE_MESSAGE_MD5_SECRET;
    }

    public static String getLiveMd5Secret() {
        return DEBUG ? LIVE_MD5_SECRET : LIVE_MD5_SECRET.toUpperCase();
    }

    public static String getStockSchool() {
        return getHDBaseURL() + "/xigua/#/school";
    }

    public static String getAppName() {
        return appName;
    }

    public static String getAppCate() {
        return appCate;
    }

    public static String getAppType() {
        return appType;
    }

    /**
     * 神策统计
     */
    private static final String TEST_SENSOR_URL = "https://test-sensors-api.baidao.com/sa?project=futures_test";
    private static final String RELEASE_SENSOR_URL = "https://jc-sensors.baidao.com/sa?project=jds_xiguazhitou&token=8uaieuh73";

    public static String getSensorUrl() {
        return DEBUG ? TEST_SENSOR_URL : RELEASE_SENSOR_URL;
    }

    public static String getSourceID() {
        return DEBUG ? "70" : "41";
    }


    public static String getJDSApi() {
        return DEBUG ? TEST_API_JDS : API_JDS;
    }

    public static String getWhetherBaseURL() {
        return DEBUG ? TEST_WHETHER_URL_NEW : RELEASE_WHETHER_URL_NEW;
    }

    private static final String TEST_WHETHER_URL_NEW = "https://api.openweathermap.org";
    private static final String RELEASE_WHETHER_URL_NEW = "https://api.openweathermap.org";

}
