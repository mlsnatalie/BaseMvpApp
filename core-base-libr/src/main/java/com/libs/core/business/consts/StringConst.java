package com.libs.core.business.consts;

import com.libs.core.BuildConfig;

public class StringConst {

    private static boolean DEBUG = BuildConfig.DEBUG;

    //etf持仓
    public static final String URL_ETF_POSITION = "http://cms.jindashi.cn/jindashi/Page/metf.html";

    //递延方向
    public static final String URL_DEFERRED_DIRECTION = "http://cms.jindashi.cn/jindashi/Page/mdeferred.html";

    private static final String URL_SUMMARY = "mvarieties/index.html?variety=%s";

    //http://hd.369wan.com/test/mvarieties/index.html?variety=Au(T+D)
    //http://hd.369wan.com/special/mvarieties/index.html?variety=Au(T+D)
    private static final String TEST_BASE_URL = "http://hd.369wan.com/test/";
    private static final String RELEASE_BASE_URL = "http://hd.369wan.com/special/";

    private static String getBaseUrl() {
        return DEBUG ? TEST_BASE_URL : RELEASE_BASE_URL;
    }

    public static String getUrlSummary() {
        return getBaseUrl() + URL_SUMMARY;
    }
}
