package com.fmx.dpuntu.utils;

import java.util.Locale;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class StringUtils {
    public static byte[] convertToBytes(String src) {

        if (src == null) {
            return null;
        }

        src = src.trim().replace(" ", "").toUpperCase(Locale.US);

        int m = 0, n = 0;
        int iLen = src.length() / 2;
        byte[] ret = new byte[iLen];

        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m)
                                                    + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }
}
