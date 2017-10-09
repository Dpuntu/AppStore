package com.seuic.app.store.bean.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class AdvertisementsReceive implements Serializable {
    @SerializedName("count")
    private String count;
    @SerializedName("interval")
    private String intervalTime;
    @SerializedName("is_hidden")
    private String isHidden;
    @SerializedName("adds")
    private List<AdReceiveDetails> adDetails;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    public List<AdReceiveDetails> getAdDetails() {
        return adDetails;
    }

    public void setAdDetails(List<AdReceiveDetails> adDetails) {
        this.adDetails = adDetails;
    }

    public static class AdReceiveDetails implements Serializable {
        @SerializedName("image")
        private String imageName;
        @SerializedName("order")
        private String order;
        @SerializedName("url")
        private String skipUrl;
        @SerializedName("is_header_included")//  0-否 1-是
        private String isAddHeader;

        public AdReceiveDetails(String imageName, String order, String skipUrl, String isAddHeader) {
            this.imageName = imageName;
            this.order = order;
            this.skipUrl = skipUrl;
            this.isAddHeader = isAddHeader;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getSkipUrl() {
            return skipUrl;
        }

        public void setSkipUrl(String skipUrl) {
            this.skipUrl = skipUrl;
        }

        public String getIsAddHeader() {
            return isAddHeader;
        }

        public void setIsAddHeader(String isAddHeader) {
            this.isAddHeader = isAddHeader;
        }

    }
}
