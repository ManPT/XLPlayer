package com.xiling.play.tools

object ShopTool {

    fun checkShopStatus(status: Int, store: Int): String {
        var result: String? =""
        result = if (status == 1) {
            if (store > 0) {
                ""
            } else {
                "已售罄"
            }
        } else {
            "已下架"
        }
        return result
    }

}