package com.libs.core.common.ACache;


import com.libs.core.common.utils.LogUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5FileNameGenerator {

    public MD5FileNameGenerator() {
    }

    public String generate(String imageUri) {
        byte[] md5 = this.getMD5(imageUri.getBytes());
        BigInteger bi = (new BigInteger(md5)).abs();
        return bi.toString(36);
    }

    private byte[] getMD5(byte[] data) {
        byte[] hash = null;

        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(data);
            hash = e.digest();
        } catch (NoSuchAlgorithmException var4) {
            LogUtils.e("MD5FileNameGenerator", "", var4);
        }

        return hash;
    }
}