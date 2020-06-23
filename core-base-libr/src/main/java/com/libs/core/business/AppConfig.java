package com.libs.core.business;

public class AppConfig {

    // 控制全局的开发环境，在Application类中初始化
    public static boolean DEBUG = false;

    public static void setDebug(boolean debug) {
        AppConfig.DEBUG = debug;
    }



    /**
     * 友盟分享
     */
    public static final String WX_APP_ID = "wx34e547421c68e712";
    public static final String WX_APP_SECRET = "13a3804f60cf527ab2eebc94089ab52b";
    public static final String QQ_APP_ID = "101406109";
    public static final String QQ_APP_KEY = "19adbdda6f46b8388e3ca1bd24236a17";
    public static final String WB_APP_ID = "2097757792";
    public static final String WB_APP_SECRET = "d69007c1b6e3e04babdaf30af8e9c713";
    public static final String WB_REDIRECT_URL = "https://passport.jindashi.cn/cas/weibo.jsp";

}
