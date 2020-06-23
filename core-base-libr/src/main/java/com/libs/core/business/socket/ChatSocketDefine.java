package com.libs.core.business.socket;

import com.libs.core.business.AppConfig;

public class ChatSocketDefine {
    private static boolean DEBUG = AppConfig.DEBUG;
//    private static boolean DEBUG = false;


//    private static final String TEST_CHAT_IP = "test-jdsim.jindashi.cn";
    private static final String TEST_CHAT_IP = "47.97.73.1";
    private static final int TEST_CHAT_PORT = 9527;

    /**
     * 行情生产环境
     */
    private static final String RELEASE_CHAT_IP = "jdsim.jindashi.cn";
    private static final int RELEASE_CHAT_PORT = 9527;

    public static String getChatIp() {
        return DEBUG ? TEST_CHAT_IP : RELEASE_CHAT_IP;
    }

    public static int getChatPort() {
        return DEBUG ? TEST_CHAT_PORT : RELEASE_CHAT_PORT;
    }

//    public static String getQuoteHttpUrl() {
//        return DEBUG ? DEBUG_QUOTE_HTTP_URL : RELEASE_QUOTE_HTTP_URL;
//    }

    public static void setDEBUG(boolean DEBUG) {
        ChatSocketDefine.DEBUG = DEBUG;
    }
}
