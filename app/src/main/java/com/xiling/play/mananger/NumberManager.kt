package com.xiling.play.mananger

import android.widget.TextView
/**
 * 对数字进行处理
 */
object NumberManager {
    /**
     * 保留两位小数
     * @param number
     * @return
     */
    fun reservedDecimalFor2(number: Double): String {
        return String.format("%.2f", number)
    }

    /**
     * 将整数和小数部分，分别赋值给不同的textView
     */
    fun setPriceText(
        number: Double,
        integerText: TextView,
        decimalView: TextView
    ) {
        val numStr = reservedDecimalFor2(number)
        val fNum = numStr.toFloat()
        integerText.text = fNum.toInt().toString() + "."
        decimalView.text = numStr.substring(numStr.length - 2, numStr.length)
    }
}