package com.libs.core.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析url 的参数
 */
public class UrlUtil {

    /**
     * 解析url
     *
     * @param url
     * @return
     */
    public static Map<String, String> parse(String url) {
        Map<String, String> result = new HashMap<>();

        if (url == null) {
            return result;
        }
        url = url.trim();
        if (url.equals("")) {
            return result;
        }
        String[] urlParts = url.split("\\?");
        //没有参数
        if (urlParts.length == 1) {
            return result;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }
}
