package com.xiling.play.bean

data class HomeBannerBean(
    var event: String,
    var sort: String,
    var target: String,
    var title: String,
    var url: String
):AutoClickBean{
    override fun getAutoEvent(): String {
        return  event
    }

    override fun getAutoTarget(): String {
        return target
    }


}