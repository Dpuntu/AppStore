package com.seuic.app.store.ui.agent;

import com.seuic.app.store.R;

/**
 * Created on 2017/10/20.
 *
 * @author dpuntu
 *         <p>
 *         仿Retrofit设计
 */

public interface ActivityService {

    /**
     * 配置主页参数
     */
    @ActivityParams(layoutId = R.layout.activity_home, isRightLayoutShow = true)
    void homeActivity();

    /**
     * 配置搜索界面参数
     */
    @ActivityParams(layoutId = R.layout.activity_search, isSearchBarFocusable = true)
    void searchActivity();

    /**
     * 配置App详情页面参数
     *
     * @param normalTitle
     *         页面Title
     */
    @ActivityParams(layoutId = R.layout.activity_appdetail, isHome = false,
            leftTitleId = R.string.act_back)
    void appDetailActivity(@ParamsBody(ParamsType.NORMAL_TITLE) String normalTitle);

    /**
     * 配置App分类页面参数
     *
     * @param normalTitle
     *         页面Title
     */
    @ActivityParams(layoutId = R.layout.activity_apptype, isHome = false,
            leftTitleId = R.string.act_back)
    void appTypeActivity(@ParamsBody(ParamsType.NORMAL_TITLE) String normalTitle);

    /**
     * 配置下载页面参数
     */
    @ActivityParams(layoutId = R.layout.activity_download, isHome = false,
            normalTitleId = R.string.act_download_set,
            leftTitleId = R.string.act_back)
    void downLoadActivity();

    /**
     * 配置本地App页面参数
     */
    @ActivityParams(layoutId = R.layout.activity_install, isHome = false,
            normalTitleId = R.string.act_install_set,
            leftTitleId = R.string.act_back)
    void installActivity();

    /**
     * 配置更新页面参数
     */
    @ActivityParams(layoutId = R.layout.activity_update, isHome = false,
            normalTitleId = R.string.act_update_title,
            leftTitleId = R.string.act_back, rightTitleId = R.string.act_update_all)
    void updateActivity();

    /**
     * 配置广告链接页面参数
     *
     * @param normalTitle
     *         页面Title
     */
    @ActivityParams(layoutId = R.layout.activity_webview, isHome = false)
    void webViewActivity(@ParamsBody(ParamsType.NORMAL_TITLE) String normalTitle);
}
