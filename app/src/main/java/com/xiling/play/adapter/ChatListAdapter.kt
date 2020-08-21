package com.xiling.play.adapter

import android.util.Log
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiling.play.R

class ChatListAdapter : BaseQuickAdapter<String, BaseViewHolder> {
    constructor() : super(R.layout.item_chat_list) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        Log.d("pangtao","bind")
        return super.onCreateViewHolder(parent, viewType)

    }


    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper!!.setText(R.id.chat_time_tv,item)
    }
}