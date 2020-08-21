package com.xiling.play.mananger

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xiling.play.R
import jp.wasabeef.glide.transformations.CropCircleTransformation

object GlideManager {

    fun loadHead(context: Context?,imageView:ImageView?,url:Any){
        Glide.with(context)
            .load(url) //此处深坑，glide bug 不加此属性，app第一次进入，图片加载不出来
            .dontAnimate()
            .bitmapTransform(CropCircleTransformation(context))
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(R.drawable.bg_image_def)
            .into(imageView)
    }

    fun loadImage(
        context: Context?,
        imageView: ImageView?,
        url: Any
    ) {
        Glide.with(context).load(url)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(R.drawable.bg_image_def)
            .fitCenter()
            .into(imageView)
    }

}