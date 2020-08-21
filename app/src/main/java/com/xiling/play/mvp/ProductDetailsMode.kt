package com.xiling.play.mvp

import com.lib.base.mvp.BaseMode
import com.mr.base.net.RequestListener
import com.xiling.play.bean.ShopDetailsBean
import com.xiling.play.http.RetrofitManager
import com.xiling.play.http.ShopService
import com.xiling.play.http.XLHttpRequest

class ProductDetailsMode : BaseMode<ProductDetailsPresenter,ProductDetailsView> {
    lateinit var shopService: ShopService

    constructor(){
        var retrofit = RetrofitManager().getMyRetrofit()
        shopService =  retrofit!!.create(ShopService::class.java)
    }


    fun requestShopDetails(spuId : String){
        XLHttpRequest.requestShopDetails(spuId,object :
            RequestListener<ShopDetailsBean> {
            override fun onStart() {

            }
            override fun onSuccess(result: ShopDetailsBean?) {
                super.onSuccess(result)
                if (result != null){
                    mPresenter!!.updateShopDetails(result)
                }
            }

            override fun onError(e: Throwable?) {
                mPresenter!!.onError(e!!.message)
            }

            override fun onComplete() {

            }
        })
    }


}