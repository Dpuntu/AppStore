package com.seuic.app.store.bean;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class RecycleSummaryBean extends RecycleTitleBean {
    private String summary;
    private String updateText;

    public RecycleSummaryBean(String title, String summary, String updateText) {
        super(title);
        this.summary = summary;
        this.updateText = updateText;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUpdateText() {
        return updateText;
    }

    public void setUpdateText(String updateText) {
        this.updateText = updateText;
    }
}
