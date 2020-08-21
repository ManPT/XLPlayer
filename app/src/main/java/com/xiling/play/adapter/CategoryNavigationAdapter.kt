package com.xiling.play.adapter

import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiling.play.R
import com.xiling.play.bean.TopCategoryBean

/**
 * 分类左侧 分类导航
 */
class CategoryNavigationAdapter :
    BaseQuickAdapter<TopCategoryBean, BaseViewHolder>(R.layout.item_category_navigation) {
    fun setmActiveIndex(mActiveIndex: Int) {
        this.mActiveIndex = mActiveIndex
        notifyDataSetChanged()
    }

    fun getmActiveIndex(): Int {
        return mActiveIndex
    }

    private var mActiveIndex = 0
    override fun convert(helper: BaseViewHolder, item: TopCategoryBean) {
        if (helper.adapterPosition == mActiveIndex) {
            helper.getView<View>(R.id.rl_category_item_root).isSelected = true
            helper.getView<View>(R.id.tv_category).isSelected = true

        } else {
            helper.getView<View>(R.id.rl_category_item_root).isSelected = false
            helper.getView<View>(R.id.tv_category).isSelected = false
        }

        if (!TextUtils.isEmpty(item.name)) {
            helper.setText(R.id.tv_category, item.name)
        }

        helper.getView<View>(R.id.rl_category_item_root).isSelected =
            item.id.equals(data[mActiveIndex].id)
    }
}