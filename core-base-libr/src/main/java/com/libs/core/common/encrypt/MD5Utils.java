package com.libs.core.common.encrypt;

import java.security.MessageDigest;

public class MD5Utils {

    /**
     * 16进制字符
     */
    private static final char HEX_CHARS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private MD5Utils() {
    }

    /**
     * 数据加密
     */
    public static String encrypt(byte[] buffer) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_CHARS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_CHARS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
