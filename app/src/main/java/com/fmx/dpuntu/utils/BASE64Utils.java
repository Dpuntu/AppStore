package com.fmx.dpuntu.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class BASE64Utils {
    private static final String CHARSET_UTF8 = "utf-8";

    public static String encrypt(String data, String charset) throws
            Exception {
        try {
            return encrypt(data.getBytes(charset), charset);
        } catch (Exception ex) {
            throw new Exception(":" + ex.getMessage(), ex);
        }
    }

    public static String encrypt(byte[] data, String charset) throws
            Exception {
        try {
            return new String(Base64.encodeBase64(data), charset);
        } catch (Exception ex) {
            throw new Exception(":" + ex.getMessage(), ex);
        }
    }

    public static String decrypt(String data, String charset) throws
            Exception {
        try {
            return decrypt(data.getBytes(charset), charset);
        } catch (Exception ex) {
            throw new Exception(":" + ex.getMessage(), ex);
        }
    }

    public static byte[] decryptByte(String data) {
        return Base64.decodeBase64(data);
    }

    public static String decrypt(byte[] data, String charset) throws
            Exception {
        try {
            return new String(Base64.decodeBase64(data), charset);
        } catch (Exception ex) {
            throw new Exception(":" + ex.getMessage(), ex);
        }
    }

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
    public static String encrypt(String data) throws Exception {
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
    public static String decrypt(String data) throws Exception {
        return decrypt(data, CHARSET_UTF8);
    }
}
