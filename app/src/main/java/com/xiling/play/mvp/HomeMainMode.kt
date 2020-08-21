package com.xiling.play.mvp

import com.lib.base.mvp.BaseMode
import com.mr.base.net.RequestListener
import com.xiling.play.bean.HomeBannerBean
import com.xiling.play.bean.HomeLiveBean
import com.xiling.play.bean.ShopListBean
import com.xiling.play.http.HomeService
import com.xiling.play.http.RetrofitManager
import com.xiling.play.http.ShopService
import com.xiling.play.http.XLHttpRequest

class HomeMainMode : BaseMode<HomeMainPresenter, HomeMainView> {
    lateinit var homeService: HomeService
    lateinit var shopService: ShopService

    constructor(){
        var retrofit = RetrofitManager().getMyRetrofit()
        homeService = retrofit!!.create(HomeService::class.java)
        shopService =  retrofit!!.create(ShopService::class.java)
    }
    /**
     * 请求首页banner
     */
    fun requestHomeBanner(){
        XLHttpRequest.requestHomeBanner(object : RequestListener<List<HomeBannerBean>>{
            override fun onStart() {

            }
            override fun onSuccess(result: List<HomeBannerBean>?) {
                super.onSuccess(result)
                if (result != null){
                    mPresenter!!.updateBanner(result)
                }
            }

            override fun onError(e: Throwable?) {
                mPresenter!!.onError(e!!.message)
            }

            override fun onComplete() {

            }
        })

    }

    /**
     * 请求首页直播列表
     */
    fun requestHomeLive(){
        XLHttpRequest.requestHomeLive(object : RequestListener<HomeLiveBean>{
            override fun onStart() {

            }

            override fun onSuccess(result: HomeLiveBean?) {
                super.onSuccess(result)
                if (result != null){
                    mPresenter!!.updateLive(result)
                }

            }

            override fun onError(e: Throwable?) {
                mPresenter!!.onError(e!!.message)
            }

            override fun onComplete() {

            }
        })
    }

    /**
     * 请求商品列表
     */
    fun requestShopList(page : Int){
        XLHttpRequest.requestShopList(page,"","","","-2",object : RequestListener<ShopListBean>{
            override fun onStart() {

            }
            override fun onSuccess(result: ShopListBean?) {
                super.onSuccess(result)
                if (result != null){
                    mPresenter!!.updateShop(result)
                }

            }
            override fun onError(e: Throwable?) {
                mPresenter!!.onError(e!!.message)
            }

            override fun onComplete() {

            }
        })
    }

}