package com.xiling.play.mananger

import android.content.Context
import android.widget.ImageView
import com.youth.banner.loader.ImageLoader

class BannerGlideLoad : ImageLoader() {
    override fun displayImage(
        context: Context,
        path: Any,
        imageView: ImageView
    ) {
       GlideManager.loadImage(context,imageView,path)
    }
}