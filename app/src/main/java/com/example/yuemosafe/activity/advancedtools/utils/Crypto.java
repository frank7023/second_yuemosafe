package com.example.yuemosafe.activity.advancedtools.utils;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yueyue on 2017/5/4.
 */

public class Crypto {
    /**
     * 加密一个文本，返回base64编码后的内容。
     *
     * @param seed  种子 密码
     * @param plain  原文
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String seed, String plain) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] encrypted = encrypt(rawKey, plain.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /**
     * 解密base64编码后的密文
     *
     * @param seed  种子 密码
     * @param encrypted  密文
     * @return 原文
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted)
            throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = Base64.decode(encrypted.getBytes(), Base64.DEFAULT);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    /**
     * 生成特定的密钥
     * @param seed  种子
     * @return  加密之后的密钥
     * @throws Exception
     */
    private static byte[] getRawKey(byte[] seed) throws Exception {
        //根据AES算法生成密钥生成器
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //跟sha1算法生成强加密的随机数生成器
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        //重新设置此随机对象的种子
        random.setSeed(seed);
        //使用用户提供的随机源初始化此密钥生成器,使其具有确定的密钥大小
        keygen.init(128, random); // 192 and 256 bits may not be available
        //生成一个密钥
        SecretKey key = keygen.generateKey();
        //返回此密钥的密钥内容
        byte[] raw = key.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] plain) throws Exception {
        // 根据给定的字节数组使用AES算法构造一个密钥
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        //Cipher类为加密和解密提供密码功能
        Cipher cipher = Cipher.getInstance("AES");
        //用密钥初始化此 cipher
        //第一个参数ENCRYPT_MODE(加密)、DECRYPT_MODE(解密)、WRAP_MODE(密钥包装) 或 UNWRAP_MODE(密钥解包)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);//第二个参数为Key类型,但Key是接口,使用其子类
        //结束多部分加密或解密操作（具体取决于此 Cipher 的初始化方式）
        byte[] encrypted = cipher.doFinal(plain);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        // 根据给定的字节数组使用AES算法构造一个密钥
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        ////Cipher类为加密和解密提供密码功能
        Cipher cipher = Cipher.getInstance("AES");
        //用密钥初始化此 cipher
        //第一个参数ENCRYPT_MODE(加密)、DECRYPT_MODE(解密)、WRAP_MODE(密钥包装) 或 UNWRAP_MODE(密钥解包)
        cipher.init(Cipher.DECRYPT_MODE, keySpec);//第二个参数为Key类型,但Key是接口,使用其子类
        //结束多部分加密或解密操作（具体取决于此 Cipher 的初始化方式）
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}