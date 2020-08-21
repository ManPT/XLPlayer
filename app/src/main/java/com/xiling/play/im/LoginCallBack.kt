package com.xiling.play.im

interface LoginCallBack {
    fun loginSuccess()
    fun loginError(code : Int,desc:String)
}