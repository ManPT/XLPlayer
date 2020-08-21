package com.xiling.play.mananger

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lib.tools.SharedPreferencesTool
import com.mr.base.tools.Tool
import java.util.*

class XLSearchWordManager() {
    val tableName = "searchShop"
    /**
     * 搜索结果的条数限制
     */
    private val size_limit = 10

    companion object{
        private  var self: XLSearchWordManager? = null
        fun share(): XLSearchWordManager? {
            if (self == null) {
                self = XLSearchWordManager()
            }
            return self
        }

    }

    private val kValue = "kValue"
    var keywords = ArrayList<String>()
    var gson: Gson? = null

    init {
        gson = Gson()
        keywords = getData()
    }



    private fun clearOldKeyword(keyword: String) {
        var target: String? = ""
        for (item in keywords) {
            if (item == keyword) {
                target = item
                break
            }
        }
        keywords.remove(target)
    }

    fun addKeyword(keyword: String) {
        clearOldKeyword(keyword)
        keywords.add(0, keyword)
        if (keywords.size > size_limit) {
            try {
                keywords = ArrayList(keywords.subList(0, size_limit))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        setData()
    }

    fun clear() {
        keywords.clear()
        setData()
    }

    fun getAll(): ArrayList<String>? {
        getData()
        return keywords
    }

    private fun getData(): ArrayList<String> {
        var value = ArrayList<String>()
        val data = SharedPreferencesTool.getData(Tool.mContext!!,tableName,kValue,"")
        value = if (TextUtils.isEmpty(data)) {
            ArrayList()
        } else {
            try {
                val type =
                    object : TypeToken<List<String?>?>() {}.type
                gson!!.fromJson<ArrayList<String>>(data, type)
            } catch (e: Exception) {
                e.printStackTrace()
                ArrayList<String>()
            }
        }
        return value
    }

    private fun setData() {
        val data = gson!!.toJson(keywords)
        SharedPreferencesTool.saveData(Tool.mContext!!,tableName,kValue,data)
    }
}