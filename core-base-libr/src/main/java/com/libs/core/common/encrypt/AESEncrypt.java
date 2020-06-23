package com.libs.core.common.encrypt;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具
 * <p>
 * Java和Android、IOS互通
 * http://www.cnblogs.com/jys509/p/4768120.html
 * http://www.cnblogs.com/happyhippy/archive/2006/12/23/601353.html
 * java和c++互通
 * http://i.actom.me/aes-cbc-%E7%9B%B8%E4%BA%92%E5%8A%A0%E8%A7%A3%E5%AF%86-javaphpc.html
 */
public class AESEncrypt {

    // 加密类型
    private static final String AES = "AES";
    // 字符格式
    private static final String UTF8 = "UTF-8";
    // 加密密钥（自定义）
    private static final String AES_KEY = "a05f9ac8e35d9f32";
    // 加密向量（自定义）
    private static final String AES_IVP = "abcdefghijklmnop";
    // 算法/模式/补码方式
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";


    /**
     * 加密
     *
     * @param encryptData 加密数据
     * @return String
     */
    public static String encrypt(byte[] encryptData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(UTF8), AES);
            IvParameterSpec ivpSpec = new IvParameterSpec(AES_IVP.getBytes());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivpSpec);
            String encodeBase64 = Base64.encodeToString(encryptData, Base64.DEFAULT);
            byte[] encrypted = cipher.doFinal(encodeBase64.getBytes(UTF8));
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param decryptData 解密数据
     * @return byte[]
     */
    public static byte[] decrypt(String decryptData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(UTF8), AES);
            IvParameterSpec ivpSpec = new IvParameterSpec(AES_IVP.getBytes());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivpSpec);
            byte[] original = cipher.doFinal(Base64.decode(decryptData, Base64.DEFAULT));
            return Base64.decode(original, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}