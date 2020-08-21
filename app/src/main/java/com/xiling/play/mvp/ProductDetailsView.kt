package com.xiling.play.mvp

import com.lib.base.mvp.BaseView
import com.xiling.play.bean.ShopDetailsBean

interface ProductDetailsView : BaseView {

    fun updateBanner(bannerList: List<String>)
    fun updateDetails(shopDetailsBean: ShopDetailsBean)
    fun updateWebView(content:String)
}