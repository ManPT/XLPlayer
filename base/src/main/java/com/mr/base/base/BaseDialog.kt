package com.mr.base.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.mr.base.R

abstract class BaseDialog : Dialog {

    val mContext: Context

    constructor(
        context: Context
    ) : super(context, R.style.BaseDialog) {
        this.mContext = context
    }

    constructor(
        context: Context,
        style: Int
    ) : super(context, style) {
        this.mContext = context
    }

    abstract fun getContentViewId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId())
        setCanceledOnTouchOutside(false)
    }

     fun setPopupOrientation(gravity: Int) {
        if (window != null) {
            window.setGravity(gravity)
            window.decorView.setPadding(0, 0, 0, 0)
            //获得window窗口的属性
            val lp = window.attributes
            //设置窗口宽度为充满全屏
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            //设置窗口高度为包裹内容
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            //将设置好的属性set回去
            window.attributes = lp
        }
    }


}