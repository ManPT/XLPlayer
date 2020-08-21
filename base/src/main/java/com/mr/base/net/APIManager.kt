package com.mr.base.net

import com.lib.net.BaseResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object APIManager {

    open fun <T> startRequest(
        observable: Observable<BaseResponse<T>?>?,
        listener: RequestListener<T>
    ): Unit {
        if (observable == null) {
            return
        }
        listener.onStart()
        observable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse<T>?> {
                override fun onComplete() {
                    listener.onComplete()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(result: BaseResponse<T>) {
                    if (listener.successCondition(result)){
                        listener.onSuccess(result.getData())
                        listener.onSuccess(result.getData(), result.getMsg())
                    }else{
                        listener.onError(Exception("返回的Code不正确  code = " + result.getCode()))
                    }
                }

                override fun onError(e: Throwable) {
                    if (e is SocketTimeoutException) {
                        listener.onError(Exception("请求超时"))
                    } else if (e is IllegalStateException) {
                        listener.onError(Exception("服务器数据异常"))
                    } else if (e is UnknownHostException) {
                        listener.onError(Exception("网络状态异常"))
                    } else {
                        listener.onError(e)
                    }
                }

            })

    }

    fun getRequestBody(json: String?): RequestBody? {
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json)
    }


}