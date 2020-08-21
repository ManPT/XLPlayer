package com.xiling.play.http

import com.lib.net.BaseResponse
import com.xiling.play.bean.HomeBannerBean
import com.xiling.play.bean.HomeLiveBean
import io.reactivex.Observable
import retrofit2.http.GET

interface HomeService {

    @GET("live/index-banner/list")
    fun getHomeBanner(
    ): Observable<BaseResponse<List<HomeBannerBean>>?>?


    @GET("live/index-live/info")
    fun getHomeLive(
    ): Observable<BaseResponse<HomeLiveBean>?>?




}