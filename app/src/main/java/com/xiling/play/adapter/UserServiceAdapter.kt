package com.xiling.play.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiling.play.R
import com.xiling.play.bean.UserServiceBeam
import com.xiling.play.mananger.GlideManager

class UserServiceAdapter : BaseQuickAdapter<UserServiceBeam, UserServiceAdapter.ViewHolder> {

    constructor() : super(R.layout.item_user_service)

    override fun convert(viewHolder: ViewHolder, item: UserServiceBeam?) {
        if (item != null) {
            GlideManager.loadImage(mContext, viewHolder.ivIcon, item.icon)

            viewHolder.tvSize.text = item.size
            viewHolder.tvServiceName.text = item.name

        }


    }


    inner class ViewHolder(view: View?) : BaseViewHolder(view) {
        lateinit var ivIcon: ImageView
        lateinit var tvSize: TextView
        lateinit var tvServiceName: TextView

        init {
            ivIcon = view!!.findViewById(R.id.ivIcon)
            tvSize = view!!.findViewById(R.id.tvSize)
            tvServiceName = view!!.findViewById(R.id.tvServiceName)
        }


    }

}