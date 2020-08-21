package com.mr.base.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mr.base.R
import kotlinx.android.synthetic.main.header_layout.view.*

class HeaderLayout : LinearLayout {

    private var mRootView: View? = null

    constructor(context: Context?) : super(context){
        initViews()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initViews()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initViews()
    }


    fun initViews(){
        hide()
        mRootView = LayoutInflater.from(context)
            .inflate(R.layout.header_layout, rootView as ViewGroup, true)
        headerTitleTv.setText(R.string.app_name)
        //默认标题栏白色
        setBackgroundColor(Color.WHITE)
    }


    fun hide() {
        this.visibility = View.GONE
    }

    fun show() {
        this.visibility = View.VISIBLE
    }

    override fun setBackgroundColor(color: Int) {
        mHeaderLayout.setBackgroundColor(color)
    }

    fun setLeftDrawable(drawable: Drawable?) {
        headerLeftIv.setImageDrawable(drawable)
        headerLeftIv.setVisibility(View.VISIBLE)
    }

    fun setLeftDrawable(@DrawableRes resId: Int) {
        if (headerLeftIv == null) {
            return
        }
        headerLeftIv.setImageResource(resId)
        headerLeftIv.setVisibility(View.VISIBLE)
    }

    fun setOnLeftClickListener(onClickListener: OnClickListener?) {
        if (headerLeftIv == null) {
            return
        }
        headerLeftTv.setOnClickListener(onClickListener)
        headerLeftIv.setOnClickListener(onClickListener)
    }

    fun setRightDrawable(drawable: Drawable?) {
        headerRightIv.setImageDrawable(drawable)
        headerRightIv.setVisibility(View.VISIBLE)
    }

    fun setRightDrawable(@DrawableRes resId: Int) {
        headerRightIv.setImageResource(resId)
        headerRightIv.setVisibility(View.VISIBLE)
    }

    fun setRightText(charSequence: CharSequence?) {
        headerRightTv.setText(charSequence)
        headerRightTv.setVisibility(View.VISIBLE)
    }

    fun setRightText(@StringRes resId: Int) {
        val string = resources.getString(resId)
        setRightText(string)
    }

    fun setOnRightClickListener(onClickListener: OnClickListener?) {
        headerRightTv.setOnClickListener(onClickListener)
        headerRightIv.setOnClickListener(onClickListener)
    }

    fun setTitleTextColor(color: Int) {
        this.headerTitleTv.setTextColor(color)
    }

    fun setTitle(@StringRes resId: Int) {
        headerTitleTv.setText(resId)
        show()
    }

    fun setTitle(charSequence: CharSequence?) {
        headerTitleTv.setText(charSequence)
        show()
    }

    fun setVisiableStatusBar(open :Boolean){
        if (open){
            statusBar.visibility =  View.VISIBLE
        }else{
            statusBar.visibility =  View.GONE
        }

    }
}