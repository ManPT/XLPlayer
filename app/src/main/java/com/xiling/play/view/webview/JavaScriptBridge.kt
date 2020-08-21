package com.xiling.play.view.webview

import android.app.Activity
import android.webkit.WebView

class JavaScriptBridge {
    companion object{
        val NAME = "XLMallBridge"
    }

    private var mActivity: Activity? = null
    private var mWebView: WebView? = null

    constructor(activity: Activity) {
        this.mActivity = activity
    }

    fun setWebView(webView: WebView) {
        mWebView = webView
    }


}