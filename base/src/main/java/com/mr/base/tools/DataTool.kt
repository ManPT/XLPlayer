package com.lib.tools

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DataTool {
    const val FORMAT = "yyyy-MM-dd HH:mm:ss"
    /**
     * 时间戳转换为需要的格式
     *
     * @param time
     * @param pattern 为空则默认yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun getDateToString(time: Long, pattern: String?): String {
        var pattern = pattern
        if (TextUtils.isEmpty(pattern)) {
            pattern = FORMAT
        }
        val format = SimpleDateFormat(pattern,
            Locale.getDefault())
        return format.format(time)
    }

    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    fun getStringToDate(dateString: String?, pattern: String?): Long {
        val dateFormat = SimpleDateFormat(pattern)
        var date = Date()
        try {
            date = dateFormat.parse(dateString)
        } catch (e: ParseException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return date.time
    }

}