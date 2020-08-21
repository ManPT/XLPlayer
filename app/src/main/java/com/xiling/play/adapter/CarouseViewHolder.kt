package com.xiling.play.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.Holder
import com.xiling.play.R
import com.xiling.play.mananger.GlideManager

class CarouseViewHolder<String> : Holder<String> {

    lateinit var imageView : ImageView
    lateinit var context: Context
    constructor(itemView: View?, context: Context) : super(itemView) {
        this.context = context
    }

    override fun updateUI(data: String) {
        GlideManager.loadImage(context,imageView,data!!)
    }

    override fun initView(itemView: View?) {
        imageView = itemView!!.findViewById(R.id.imageView)
    }
}