package com.xiling.play.http

import android.text.TextUtils
import com.mr.base.net.APIManager
import com.mr.base.net.RequestListener
import com.xiling.play.bean.*
import org.json.JSONObject

object XLHttpRequest {
    lateinit var homeService: HomeService
    lateinit var shopService: ShopService

    val PAGE_SIZE = 15

    init {
        var retrofit = RetrofitManager().getMyRetrofit()
        homeService = retrofit!!.create(HomeService::class.java)
        shopService = retrofit!!.create(ShopService::class.java)
    }

    /**
     * 请求首页banner
     */
    fun requestHomeBanner(requestListener: RequestListener<List<HomeBannerBean>>) {
        APIManager.startRequest(homeService.getHomeBanner(), requestListener)
    }

    /**
     * 请求首页直播列表
     */
    fun requestHomeLive(requestListener: RequestListener<HomeLiveBean>) {
        APIManager.startRequest(homeService.getHomeLive(), requestListener)
    }


    /**
     * 请求商品列表
     */
    fun requestShopList(
        page: Int,
        categoryId: String?, keyWord: String?, roomId: String?, topStatus: String,
        requestListener: RequestListener<ShopListBean>
    ) {
        val jsonObject = JSONObject()

        if (!TextUtils.isEmpty(categoryId)) {
            jsonObject.put("categoryId", categoryId)
        }

        if (!TextUtils.isEmpty(keyWord)) {
            jsonObject.put("keyWord", keyWord)
        }

        if (!TextUtils.isEmpty(roomId)) {
            jsonObject.put("roomId", roomId)
        }

        if (!TextUtils.isEmpty(topStatus)) {
            jsonObject.put("topStatus", topStatus)
        }

        APIManager.startRequest(
            shopService.getShopList(
                page,
                PAGE_SIZE,
                APIManager.getRequestBody(jsonObject.toString())
            ), requestListener
        )
    }

    /**
     * 获取一级分类列表（左侧）
     */
    fun requestTopCategory(requestListener: RequestListener<List<TopCategoryBean>>) {
        APIManager.startRequest(shopService.getCategoryList(), requestListener)
    }


    /**
     * 获取一级分类列表（左侧）
     */
    fun requestShopDetails(spuId:String,requestListener: RequestListener<ShopDetailsBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("spuId",spuId)

        APIManager.startRequest(shopService.getShopDetails(APIManager.getRequestBody(jsonObject.toString())),requestListener)
    }


}