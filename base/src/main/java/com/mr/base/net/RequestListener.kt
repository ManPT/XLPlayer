package com.mr.base.net

import com.lib.net.BaseResponse

interface RequestListener<E> {
    fun onStart()

    fun onSuccess(result: E?) {}

    fun onSuccess(result: E?, msg: String?) {}

    fun onError(e: Throwable?)

    fun onError(e: Throwable?, businessCode: String?) {}

    fun onComplete()

    fun successCondition(result: BaseResponse<E>):Boolean{
        return  result.getCode() == 200
    }
}