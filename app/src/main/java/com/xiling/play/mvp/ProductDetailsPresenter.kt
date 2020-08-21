package com.xiling.play.mvp

import com.lib.base.mvp.BasePresenter
import com.xiling.play.bean.ShopDetailsBean

class ProductDetailsPresenter : BasePresenter<ProductDetailsView>() {
    fun updateShopDetails(shopDetailsBean: ShopDetailsBean){
        var imgUrls : List<String> = shopDetailsBean.imgUrls
        if (imgUrls == null){
            imgUrls = ArrayList()
        }

        mView!!.updateBanner(imgUrls)
        mView!!.updateDetails(shopDetailsBean)
        mView!!.updateWebView(shopDetailsBean.content)
    }
}