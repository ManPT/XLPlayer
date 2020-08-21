package com.xiling.play.mananger

import android.content.Context
import android.net.Uri
import com.xiling.play.bean.AutoClickBean
import com.xiling.play.view.webview.WebViewActivity

object AutoClickManager {

    /**
     * 解析
     */

    fun pars(context: Context?, autoClickBean: AutoClickBean) {
        when (autoClickBean.getAutoEvent()) {
            "link" -> WebViewActivity.jumpUrl(context!!, autoClickBean.getAutoTarget())
            "native" -> compileNative(context!!, autoClickBean.getAutoTarget())
        }
    }

    private fun compileNative(
        context: Context,
        target: String
    ) {
        val parse = Uri.parse("app://$target")
        when (parse.host) {

        }
    }

}