package com.xiling.play.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiling.play.R
import com.xiling.play.bean.TableLayoutBean

class TableLayoutAdapter : BaseQuickAdapter<TableLayoutBean, BaseViewHolder> {
    constructor() : super(R.layout.item_tablelayout) {


    }

    override fun convert(helper: BaseViewHolder?, item: TableLayoutBean?) {
        helper!!.setText(R.id.tv_text,item!!.name)
        item!!.icon?.let { helper.setBackgroundRes(R.id.iv_icon, it) }
        helper.getView<ImageView>(R.id.iv_icon).isEnabled = item.isEnable
        helper.getView<View>(R.id.tv_text).isEnabled = item.isEnable
    }



}