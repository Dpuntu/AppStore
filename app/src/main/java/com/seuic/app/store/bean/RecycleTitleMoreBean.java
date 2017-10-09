package com.seuic.app.store.bean;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class RecycleTitleMoreBean extends RecycleTitleBean {
    /**
     * 是否显示更多按钮
     */
    private boolean isShowMore;
    /**
     * 分类的ID
     */
    private String assortmentId;

    public RecycleTitleMoreBean(String title, boolean isShowMore, String assortmentId) {
        super(title);
        this.isShowMore = isShowMore;
        this.assortmentId = assortmentId;
    }

    public boolean isShowMore() {
        return isShowMore;
    }

    public void setShowMore(boolean showMore) {
        isShowMore = showMore;
    }

    public String getAssortmentId() {
        return assortmentId;
    }

    public void setAssortmentId(String assortmentId) {
        this.assortmentId = assortmentId;
    }
}
