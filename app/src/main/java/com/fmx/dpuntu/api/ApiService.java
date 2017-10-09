package com.fmx.dpuntu.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public interface ApiService {

    /**
     * 请求APP列表
     */
    @GET("applist")
    Observable<Response<AppListResponse>> appList(@Query("TermHardType") String TermHardType,
                                                  @Query("TermIMEI") String TermIMEI,
                                                  @Query("TermType") String TermType,
                                                  @Query("TermVer") String TermVer,
                                                  @Query("page") String page,
                                                  @Query("rows") String rows);
}
