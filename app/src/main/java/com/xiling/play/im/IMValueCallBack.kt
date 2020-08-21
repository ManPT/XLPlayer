package com.xiling.play.im

interface IMValueCallBack<T> {
    fun success(value:T)
    fun error(code : Int,desc:String)
}