package com.xiling.play.mvp

import com.lib.base.mvp.BasePresenter
import com.xiling.play.bean.ShopListBean
import com.xiling.play.bean.TopCategoryBean

class ClassificationPresenter : BasePresenter<ClassificationView>() {

    fun updateCategoryList(categoryList: List<TopCategoryBean>){
            mView!!.updateCategoryList(categoryList)
    }

    /**
     * 更新商品列表
     */
    fun updateShop(shopListBean: ShopListBean){
        mView!!.updateSecondCategory(shopListBean.totalPage,shopListBean.rows)
    }

}