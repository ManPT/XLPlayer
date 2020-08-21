package com.xiling.play.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bigkoo.convenientbanner.ConvenientBanner

class SquareBanner : ConvenientBanner<String> {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        val dimen = View.getDefaultSize(0, widthMeasureSpec)
        setMeasuredDimension(dimen, dimen)
    }

}