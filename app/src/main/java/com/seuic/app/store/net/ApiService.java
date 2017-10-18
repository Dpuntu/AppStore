package com.seuic.app.store.net;

import com.seuic.app.store.bean.request.AppVersionRequest;
import com.seuic.app.store.bean.response.AdvertisementsReceive;
import com.seuic.app.store.bean.response.AppDetailReceive;
import com.seuic.app.store.bean.response.AppTypeReceive;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.bean.response.ResponseData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public interface ApiService {
    /**
     * 获得广告位信息
     */
    @GET("appstore/advertisements")
    Observable<ResponseData<AdvertisementsReceive>> getAdvertisements();

    /**
     * 获得推荐应用
     */
    @GET("appstore/recommended")
    Observable<ResponseData<List<RecommendReceive>>> getRecommendApps();


    /**
     * 获得分类应用
     */
    @GET("appstore/types")
    Observable<ResponseData<List<AppTypeReceive>>> getAppTypes();

    @Deprecated
    /**
     * 获得某一类所有的APP
     *
     * @param typeId
     *         分类ID
     */
    @GET("appstore/type")
    Observable<ResponseData<AppTypeReceive>> getTypeApps(
            @Query("type_id") String typeId);

    /**
     * 获得某一APP的详情
     *
     * @param appPackageName
     *         APP包名
     */
    @GET("appstore/appdetail")
    Observable<ResponseData<AppDetailReceive>> getAppDetail(
            @Query("app_package_name") String appPackageName);


    /**
     * 检查更新
     *
     * @param appVersionRequests
     *         请求体
     */
    @POST("appstore/updateinfo")
    Observable<ResponseData<List<RecommendReceive>>> checkUpdate(
            @Body() List<AppVersionRequest> appVersionRequests);

    /**
     * 搜索app
     *
     * @param searchName
     *         搜索名
     */
    @GET("appstore/search")
    Observable<ResponseData<List<AppDetailReceive>>> searchApps(
            @Query("search_name") String searchName);

    /**
     * 搜索app关键字
     *
     * @param searchName
     *         搜索名
     */
    @GET("appstore/search_note")
    Observable<ResponseData<List<String>>> searchAppsNote(
            @Query("search_name") String searchName);

    /**
     * 安装反馈
     */
    @POST("appstore/installresult")
    Observable<ResponseData<String>> installResult(
            @Query("app_package_name") String appPackageName,
            @Query("app_version_id") String appVersionId);
}
