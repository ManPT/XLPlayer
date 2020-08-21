package com.xiling.play.mvp

import com.lib.base.mvp.BasePresenter
import com.xiling.play.bean.HomeBannerBean
import com.xiling.play.bean.HomeLiveBean
import com.xiling.play.bean.ShopListBean

class HomeMainPresenter : BasePresenter<HomeMainView>() {
    /**
     * 更新Banner
     */
    fun updateBanner(bannerList: List<HomeBannerBean>){
        var imgList = ArrayList<String>()
        for (homeBannerBean : HomeBannerBean in bannerList){
            imgList.add(homeBannerBean.url)
        }
        mView!!.updateBannerView(imgList)
        mView!!.setBannerViewClickListener(bannerList)
    }

    /**
     * 更新直播列表
     */
    fun updateLive(homeLiveBean : HomeLiveBean){
        if (homeLiveBean.indexHot != null){
            mView!!.updateLiveRoom(homeLiveBean.indexHot)
        }

        mView!!.updateLiveList(homeLiveBean.indexHotList)
    }

    /**
     * 更新商品列表
     */
    fun updateShop(shopListBean: ShopListBean){
        mView!!.updateShopList(shopListBean.totalPage,shopListBean.rows)
    }



}