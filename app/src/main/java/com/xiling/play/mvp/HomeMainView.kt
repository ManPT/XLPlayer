package com.xiling.play.mvp

import com.lib.base.mvp.BaseView
import com.xiling.play.bean.AutoClickBean
import com.xiling.play.bean.IndexHot
import com.xiling.play.bean.IndexHotX
import com.xiling.play.bean.Row

interface HomeMainView : BaseView {

    fun updateBannerView(imgsList : List<String>)
    fun setBannerViewClickListener(autoList:List<AutoClickBean>)
    fun updateLiveRoom(indexHot : IndexHot?)
    fun updateLiveList(liveList:List<IndexHotX>?)
    fun updateShopList(totalPage : Int,rows : List<Row>)
}