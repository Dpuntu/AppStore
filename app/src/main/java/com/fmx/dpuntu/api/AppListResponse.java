package com.fmx.dpuntu.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class AppListResponse implements Serializable {
    @SerializedName("total")
    private String total;

    @SerializedName("rows")
    private List<DownloadAppInfo> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DownloadAppInfo> getRows() {
        return rows;
    }

    public void setRows(List<DownloadAppInfo> rows) {
        this.rows = rows;
    }

    public static class DownloadAppInfo implements Serializable {

        /**
         * "AppId": "0000000008",
         * "AppName": "美团外卖",
         * "IconUrl": "1825CB795F0BC31EFC78CA3AAFDF594226AF2A70E3617DFB2B6744FCC132804E92DB4423411DFABE3787B1FC67897D047F7EBCB2B051A46F3C02E8DCA7CC00EEFAEF70C11585FF0F",
         * "AppUrl":
         * "1825CB795F0BC31EFC78CA3AAFDF594226AF2A70E3617DFB2B6744FCC132804E92DB4423411DFABEA18F72589DE9C5D893DD1B5D7A1D223B851158A7975FEFCE174F625B88AB3A59650535D5CD544D73EE33FF1D6BFE235070D6FF2393869519",
         * "AppPackageName": "com.sankuai.meituan.meituanwaimaibusiness",
         * "AppSize": "5.45MB",
         * "AppVersion": "3.5.0.255",
         * "AppHashCode": "D88C32E781E12C9A71D263243CDD2029",
         * "AppComment": "北企商城应用"
         */

        @SerializedName("AppId")
        private String AppId;

        @SerializedName("AppName")
        private String AppName;

        @SerializedName("IconUrl")
        private String IconUrl;

        @SerializedName("AppUrl")
        private String AppUrl;

        @SerializedName("AppPackageName")
        private String AppPackageName;

        @SerializedName("AppSize")
        private String AppSize;

        @SerializedName("AppVersion")
        private String AppVersion;

        @SerializedName("AppHashCode")
        private String AppHashCode;

        @SerializedName("AppComment")
        private String AppComment;

        public String getAppId() {
            return AppId;
        }

        public void setAppId(String appId) {
            AppId = appId;
        }

        public String getAppName() {
            return AppName;
        }

        public void setAppName(String appName) {
            AppName = appName;
        }

        public String getIconUrl() {
            return IconUrl;
        }

        public void setIconUrl(String iconUrl) {
            IconUrl = iconUrl;
        }

        public String getAppUrl() {
            return AppUrl;
        }

        public void setAppUrl(String appUrl) {
            AppUrl = appUrl;
        }

        public String getAppPackageName() {
            return AppPackageName;
        }

        public void setAppPackageName(String appPackageName) {
            AppPackageName = appPackageName;
        }

        public String getAppSize() {
            return AppSize;
        }

        public void setAppSize(String appSize) {
            AppSize = appSize;
        }

        public String getAppVersion() {
            return AppVersion;
        }

        public void setAppVersion(String appVersion) {
            AppVersion = appVersion;
        }

        public String getAppHashCode() {
            return AppHashCode;
        }

        public void setAppHashCode(String appHashCode) {
            AppHashCode = appHashCode;
        }

        public String getAppComment() {
            return AppComment;
        }

        public void setAppComment(String appComment) {
            AppComment = appComment;
        }
    }
}
