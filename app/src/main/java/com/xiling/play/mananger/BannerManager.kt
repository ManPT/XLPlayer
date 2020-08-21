package com.xiling.play.mananger

import android.view.View
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer

object BannerManager {
    fun startBanner(
        banner: Banner,
        bannerList: List<String?>
    ) {
        if (bannerList.size > 0) {
            banner.visibility = View.VISIBLE
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            banner.setIndicatorGravity(BannerConfig.CENTER)
            banner.setImageLoader(BannerGlideLoad())
            banner.setDelayTime(2000)
            banner.setImages(bannerList)
            banner.setBannerAnimation(Transformer.Default)
            banner.start()
        } else {
            banner.visibility = View.GONE
        }
    }
}