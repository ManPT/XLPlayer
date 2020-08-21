package com.xiling.play.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiling.play.R
import com.xiling.play.bean.IndexHotX
import com.xiling.play.mananger.GlideManager

class HomeLiveAdapter : BaseQuickAdapter<IndexHotX,BaseViewHolder> {
    constructor() : super(R.layout.item_home_live)

    override fun convert(helper: BaseViewHolder?, item: IndexHotX?) {
        if (item != null){
            GlideManager.loadImage(mContext,helper!!.getView(R.id.ivLiveItem),item!!.imgUrl)
        }

    }
}