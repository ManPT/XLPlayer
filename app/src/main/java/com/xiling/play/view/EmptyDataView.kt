package com.xiling.play.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.xiling.play.R

class EmptyDataView : LinearLayout {
    private lateinit var mImageView: ImageView
    private lateinit var mTextView: TextView
    private lateinit var mReloadView: TextView

    constructor(context: Context?) : super(context){
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initView()
    }

    private fun initView(){
        View.inflate(context, R.layout.cmp_no_data, this)
        mImageView = findViewById(R.id.noDataIcon)
        mTextView = findViewById(R.id.noDataLabel)
        mReloadView = findViewById(R.id.tvReRefresh)

    }

    fun setImgRes(resId: Int):EmptyDataView {
        mImageView.setImageResource(resId)
        return this
    }

    fun setTextView(str: String?): EmptyDataView {
        mTextView.text = str
        return this
    }

    fun setReload(
        text: String,
        listener: OnClickListener?
    ): EmptyDataView? {
        mReloadView.text = "" + text
        mReloadView.setOnClickListener(listener)
        mReloadView.visibility = View.VISIBLE
        return this
    }


}