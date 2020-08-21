package com.xiling.play.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.mr.base.base.BaseDialog
import com.xiling.play.R

class SkuSelectDialog(context: Context) : BaseDialog(context) {

    override fun getContentViewId(): Int {
        return R.layout.dialog_sku_select
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPopupOrientation(Gravity.BOTTOM)
        ButterKnife.bind(this)

    }


    @OnClick(R.id.btnOK, R.id.tv_close)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btnOK -> {
                dismiss()
            }
            R.id.tv_close -> {
                dismiss()
            }
        }
    }


    interface OnSelectListener{
        fun select(size:Int)

    }


}