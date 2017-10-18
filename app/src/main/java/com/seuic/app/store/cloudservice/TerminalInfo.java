package com.seuic.app.store.cloudservice;

/**
 * Created on 2017/10/16.
 *
 * @author dpuntu
 */

public class TerminalInfo {
    /*设备编号*/
    private String serialNo;
    /*设备型号*/
    private String modelNo;
    /*厂商编号*/
    private String facNo;
    /*加密芯片信息*/
    private String mcuInfo;
    /*加密芯片状态*/
    private String mcuStatus;
    /*是否低电量*/
    private boolean lackPower;
    /*是否低纸量*/
    private boolean lackPaper;
    /*电量百分比*/
    private String powerPercent;
    /* CloudService 版本 */
    private String cloudSdk;
    /* PosdService 版本 */
    private String posdSdk;

    public String getNativeVersion() {
        return nativeVersion;
    }

    public void setNativeVersion(String nativeVersion) {
        this.nativeVersion = nativeVersion;
    }

    private String nativeVersion;

    public String getPosdSdk() {
        return posdSdk;
    }

    public void setPosdSdk(String posdSdk) {
        this.posdSdk = posdSdk;
    }

    public String getCloudSdk() {
        return cloudSdk;
    }

    public void setCloudSdk(String cloudSdk) {
        this.cloudSdk = cloudSdk;
    }

    public String getPowerPercent() {
        return powerPercent;
    }

    public void setPowerPercent(String powerPercent) {
        this.powerPercent = powerPercent;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getFacNo() {
        return facNo;
    }

    public void setFacNo(String facNo) {
        this.facNo = facNo;
    }

    public String getMcuInfo() {
        return mcuInfo;
    }

    public void setMcuInfo(String mcuInfo) {
        this.mcuInfo = mcuInfo;
    }

    public String getMcuStatus() {
        return mcuStatus;
    }

    public void setMcuStatus(String mcuStatus) {
        this.mcuStatus = mcuStatus;
    }

    public boolean isLackPower() {
        return lackPower;
    }

    public void setLackPower(boolean lackPower) {
        this.lackPower = lackPower;
    }

    public boolean isLackPaper() {
        return lackPaper;
    }

    public void setLackPaper(boolean lackPaper) {
        this.lackPaper = lackPaper;
    }
}
