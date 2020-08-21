package com.xiling.play.mvp

import com.lib.base.mvp.BaseView
import com.xiling.play.bean.Row
import com.xiling.play.bean.TopCategoryBean

interface ClassificationView : BaseView {

    fun updateCategoryList(categoryList: List<TopCategoryBean>)

    fun updateSecondCategory(totalPage : Int,rows : List<Row>)
}