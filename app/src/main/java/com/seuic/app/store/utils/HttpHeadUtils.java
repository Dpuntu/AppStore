package com.seuic.app.store.utils;

import java.util.HashMap;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 */

public class HttpHeadUtils {
    public static String HEAD_TIME = "time";
    public static String HEAD_RANDOM = "randomstr";
    public static String HEAD_SN = "sn";
    public static String HEAD_PRS = "prs";
    public static String HEAD_CHANNEL = "channel";
    public static String HEAD_SIGN = "sign";
    public static String HEAD_CONNECTION = "Connection";

    public static HashMap<String, String> getHeadMap() {// 由于每次都需要变动，所以无法做到一次初始化次次使用
        HashMap<String, String> headMap = new HashMap<>();
        String time = AndroidUtils.systemTime();
        String randomStr = StringUtils.createRandom(false, 14);
        String sn = AndroidUtils.getSerial();
//        String sn = "0F81CE33-0733-1246-DC1D-E2881D7392FE";// 测试SN
        String prs = AndroidUtils.getCustomer();
        String channel = AppStoreUtils.CHANNEL;
        String sign = "time=" + time + "&"
                + "randomstr=" + randomStr + "&"
                + "sn=" + sn + "&"
                + "prs=" + prs + "&"
                + "channel=" + channel + "&"
                + "key=" + AppStoreUtils.MD5_KEY;
        String base64Sign = BASE64Utils.encrypt(Md5Utils.getMD5(sign));
        headMap.put(HEAD_TIME, time);
        headMap.put(HEAD_RANDOM, randomStr);
        headMap.put(HEAD_SN, sn);
        headMap.put(HEAD_PRS, prs);
        headMap.put(HEAD_CHANNEL, channel);
        headMap.put(HEAD_SIGN, base64Sign);
        headMap.put(HEAD_CONNECTION, "close");
        return headMap;
    }

}
