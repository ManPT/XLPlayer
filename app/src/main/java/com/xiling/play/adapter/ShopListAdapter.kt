package com.xiling.play.adapter

import android.graphics.Paint
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiling.play.R
import com.xiling.play.activity.XLProductDetailsActivity
import com.xiling.play.bean.Row
import com.xiling.play.mananger.GlideManager
import com.xiling.play.mananger.NumberManager
import com.xiling.play.tools.ShopTool
import com.xiling.play.view.AutoLayoutManager

class ShopListAdapter : BaseQuickAdapter<Row, ShopListAdapter.ShopViewHolder> {

    lateinit var tagsAdapter: ShopListTagsAdapter

    constructor(layout: Int) : super(layout){

        this.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                XLProductDetailsActivity.jump(mContext,data.get(position).spuId.toString())
            }

        })

    }


    override fun convert(holder: ShopViewHolder, item: Row) {
        setStatus(holder,1,item.stock)

        if (!TextUtils.isEmpty(item!!.coverImg)) {
            GlideManager.loadImage(mContext, holder.ivThumb, item.coverImg)
        }

        if (!TextUtils.isEmpty(item.spuName)) {
            holder.tvTitle.text = item.spuName
        }

        if (!TextUtils.isEmpty(item.labelName)){
            var tags: List<String> = item.labelName.split(",")
            tagsAdapter.setNewData(tags)
        }else{
            tagsAdapter.setNewData(ArrayList<String>())
        }


        NumberManager.setPriceText(
            item.salePrice,
            holder.tvDiscountPrice,
            holder.tvDiscountPriceDecimal
        )
        NumberManager.setPriceText(
            item.originalPrice,
            holder.tvMinmarketPrice,
            holder.tvDiscountPriceDecimal
        )
        holder.tvMinmarketPrice.text = (NumberManager.reservedDecimalFor2(item.originalPrice))
    }


    private fun setStatus(holder: ShopViewHolder,status : Int,stock : Int){
        val mStatus: String =ShopTool.checkShopStatus(status,stock)
        if (!TextUtils.isEmpty(mStatus)){
            // 已售罄
            holder.tvStatus.visibility = View.VISIBLE
            holder.tvStatus.text = mStatus
            holder.tvRMB.isEnabled = false
            holder.tvDiscountPrice.isEnabled = false
            holder.tvDiscountPriceDecimal.isEnabled =false
        }else{
            holder.tvStatus.visibility = View.GONE
            holder.tvRMB.isEnabled = true
            holder.tvDiscountPrice.isEnabled = true
            holder.tvDiscountPriceDecimal.isEnabled =true
        }
    }

    inner class ShopViewHolder(view: View?) : BaseViewHolder(view) {
        lateinit var ivThumb: ImageView
        lateinit var tvTitle: TextView
        lateinit var tvDiscountPrice: TextView
        lateinit var tvDiscountPriceDecimal: TextView
        lateinit var tvMinmarketPrice: TextView
        lateinit var recyclerTags: RecyclerView
        lateinit var tvStatus : TextView
        lateinit var tvRMB : TextView
        init {
            tvStatus = view!!.findViewById(R.id.tv_status)
            ivThumb = view!!.findViewById(R.id.iv_thumb)
            tvTitle = view!!.findViewById(R.id.tv_title)
            tvRMB = view!!.findViewById(R.id.tv_rmb)
            tvDiscountPrice = view!!.findViewById(R.id.tv_discount_price)
            tvDiscountPriceDecimal = view!!.findViewById(R.id.tv_discount_price_decimal)
            tvMinmarketPrice = view!!.findViewById(R.id.tv_minMarketPrice)
            tvMinmarketPrice.getPaint().setAntiAlias(true) //抗锯齿
            tvMinmarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            recyclerTags = view!!.findViewById(R.id.recycler_tags)
            val autoLayoutManager = AutoLayoutManager()
            autoLayoutManager.setAutoMeasureEnabled(true)
            recyclerTags.setLayoutManager(autoLayoutManager)
            tagsAdapter = ShopListTagsAdapter(R.layout.item_shop_list_tag)
            recyclerTags.adapter = tagsAdapter
        }
    }
}