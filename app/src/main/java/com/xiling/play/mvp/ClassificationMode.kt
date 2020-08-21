package com.xiling.play.mvp

import com.lib.base.mvp.BaseMode
import com.mr.base.net.RequestListener
import com.xiling.play.bean.ShopListBean
import com.xiling.play.bean.TopCategoryBean
import com.xiling.play.http.RetrofitManager
import com.xiling.play.http.ShopService
import com.xiling.play.http.XLHttpRequest

class ClassificationMode : BaseMode<ClassificationPresenter,ClassificationView> {
    lateinit var shopService: ShopService

    constructor(){
        var retrofit = RetrofitManager().getMyRetrofit()
        shopService =  retrofit!!.create(ShopService::class.java)
    }

    /**
     * 获取一级分类列表（左侧）
     */
    fun requestTopCategory(){
        XLHttpRequest.requestTopCategory(object :
            RequestListener<List<TopCategoryBean>> {
            override fun onStart() {

            }
            override fun onSuccess(result: List<TopCategoryBean>?) {
                super.onSuccess(result)
                if (result != null){
                    mPresenter!!.updateCategoryList(result)
                }
            }

            override fun onError(e: Throwable?) {
                mPresenter!!.onError(e!!.message)
            }

            override fun onComplete() {

            }
        })

    }

    fun requestSecondCategory(nodeId :String,page : Int){
        XLHttpRequest.requestShopList(page,nodeId,"","","",object : RequestListener<ShopListBean>{
            override fun onStart() {

            }

            override fun onSuccess(result: ShopListBean?) {
                super.onSuccess(result)
                if (result != null){
                    mPresenter!!.updateShop(result)
                }
            }

            override fun onError(e: Throwable?) {

            }

            override fun onComplete() {

            }

        })
    }
}