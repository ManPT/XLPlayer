package com.xiling.play.http

import com.lib.net.BaseResponse
import com.xiling.play.bean.ShopDetailsBean
import com.xiling.play.bean.ShopListBean
import com.xiling.play.bean.TopCategoryBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ShopService {

    @POST("live/spu/list")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getShopList(@Query("start") start:Int,
                    @Query("size") size:Int,
                    @Body body : RequestBody?): Observable<BaseResponse<ShopListBean>?>?


    @GET("live/category/list")
    fun getCategoryList(): Observable<BaseResponse<List<TopCategoryBean>>?>?

    @POST("live/spu/getSpuDetail")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getShopDetails(@Body body : RequestBody?): Observable<BaseResponse<ShopDetailsBean>?>?

}