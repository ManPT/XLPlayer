package com.xiling.play.http

import com.lib.net.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface UserService {

    /**
     * 登录接口
     * http://192.168.1.123:8080/live/login/mock?code=AAAA0000
     */
    @GET("live/login/mock?code=AAAA0000")
    fun login(
    ): Observable<BaseResponse<Any>?>?




}