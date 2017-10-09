package com.seuic.app.store.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 */

public class BASE64Utils {
    private static final String CHARSET_UTF8 = "utf-8";

    /**
     * 加密
     * *
     *
     * @param data
     *
     * @return
     *
     * @throws Exception
     */
    public static String encrypt(String data) {
        return encrypt(data, CHARSET_UTF8);
    }

    /**
     * 解密
     * *
     *
     * @param data
     *
     * @return
     *
     * @throws Exception
     */
    public static String decrypt(String data) {
        return decrypt(data, CHARSET_UTF8);
    }

    public static String encrypt(String data, String charset) {
        try {
            return encrypt(data.getBytes(charset), charset);
        } catch (Exception e) {
            Loger.e(android.util.Log.getStackTraceString(e));
            return null;
        }
    }

    public static String decrypt(String data, String charset) {
        try {
            return decrypt(data.getBytes(charset), charset);
        } catch (Exception e) {
            Loger.e(android.util.Log.getStackTraceString(e));
            return null;
        }
    }

    public static String encrypt(byte[] data, String charset) {
        try {
            return new String(Base64.encodeBase64(data), charset);
        } catch (Exception e) {
            Loger.e(android.util.Log.getStackTraceString(e));
            return null;
        }
    }

    public static String decrypt(byte[] data, String charset) {
        try {
            return new String(Base64.decodeBase64(data), charset);
        } catch (Exception e) {
            Loger.e(android.util.Log.getStackTraceString(e));
            return null;
        }
    }
}
