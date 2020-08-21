package com.xiling.play.player

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

class ResizeAbleSurfaceView : SurfaceView {

    private var mWidth = -1
    private var mHeight = -1

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mWidth == -1 ){
            mWidth = widthMeasureSpec
        }

        if (mHeight == -1 ){
            mHeight = heightMeasureSpec
        }
        setMeasuredDimension(widthMeasureSpec, mHeight)
    }

    fun resize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
       // holder.setFixedSize(width, height)
        requestLayout()
        invalidate()
    }
}