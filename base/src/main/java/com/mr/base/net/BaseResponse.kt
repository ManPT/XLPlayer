package com.lib.net

class BaseResponse<T> {
    private var code = 0
    private var data:T? = null
    private var errorMessage:String = ""
    private var timestamp : Long = 0

    public fun getCode():Int{
        return code;
    }

    public fun getData():T?{
        return data;
    }

    public fun getMsg():String{
        return errorMessage;
    }

    fun getTimeStamp():Long{
        return timestamp
    }
}