package com.xiling.play.login

import android.util.Log
import com.blankj.utilcode.utils.SPUtils
import com.google.gson.Gson

object UserManager {
    val USER_TABLE = "user_table_name"
    private var userManager: UserManager? = null
    private lateinit var spUtils: SPUtils
    private var gson: Gson? = null
    //val OAUTH = "__outh"
    val OAUTH = "SESSION"

    init {
        try {
            spUtils = SPUtils(USER_TABLE)
            gson = Gson()
        }catch (e:Exception){
            Log.d("","")
        }

    }



    fun setOAuthToken(token: String?) {
        spUtils.putString(OAUTH, token)
    }

    fun getOAuthToken() : String?{
        return spUtils.getString(OAUTH)?:""
    }

}