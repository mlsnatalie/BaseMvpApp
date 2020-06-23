package com.libs.core.common.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MD5加密工具类
 */
public class MD5Encrypt {

    // 加密类型
    private static final String MD5 = "MD5";
    // 字符格式
    private static final String UTF8 = "UTF-8";


    /**
     * MD5签名
     *
     * @param secret  秘钥
     * @param paraMap 参数
     * @return String
     */
    public static String signByMd5(String secret, Map<String, String> paraMap) {
        try {
            String signContent = getContent(secret, paraMap);
            return hash(signContent);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param signContent 验签内容
     * @return String
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static String hash(String signContent) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(MD5);
        md.update(signContent.getBytes(UTF8));
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte data : bytes) {
            sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString().toLowerCase();
    }


    /**
     * 获取参数串
     *
     * @param secret  秘钥
     * @param paraMap 参数
     * @return String
     */
    private static String getContent(String secret, Map<String, String> paraMap) {
        Object[] keyArray = paraMap.keySet().toArray();
        List<String> keyList = new ArrayList<>();
        for (Object key : keyArray) {
            if ("sign".equals(key))
                continue;
            if ("callback".equals(key))
                continue;
            keyList.add((String) key);
        }
        Collections.sort(keyList);
        StringBuilder signContent = new StringBuilder();
        for (String key : keyList) {
            signContent.append(key).append("=").append(paraMap.get(key)).append("&");
        }
        signContent.deleteCharAt(signContent.length() - 1);
        signContent.append(secret);
        return signContent.toString();
    }


    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("api_version", "1.0");
        params.put("api_id", "999999");
        params.put("oid", "352575071991660");
        params.put("source", "android");
        params.put("timestamp", "1477892707861");
        params.put("verifymethod", "md5");
        params.put("version", "v1.3.6");
        String sign = signByMd5("123456", params);
        System.out.println("sign=" + sign);
        params.put("sign", signByMd5("123456", params));
        System.out.println(params);
    }

}
