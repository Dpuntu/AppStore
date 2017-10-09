package com.seuic.app.store.utils;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 */

public class StringUtils {


    /**
     * 产生随机字符串
     *
     * @param numberFlag
     *         是否随机数字
     * @param length
     *         产生随机字符串的长度
     *
     * @return String
     */
    public static String createRandom(boolean numberFlag, int length) {
        StringBuffer retStr = null;
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnopqrstuvwxyz_";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = new StringBuffer();
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr.append(strTable.charAt(intR));
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr.toString();
    }
}
