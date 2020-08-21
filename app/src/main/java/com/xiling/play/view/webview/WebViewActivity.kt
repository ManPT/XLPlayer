package com.xiling.play.view.webview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import com.blankj.utilcode.utils.StringUtils
import com.blankj.utilcode.utils.ToastUtils
import com.just.library.AgentWeb
import com.just.library.ChromeClientCallbackManager
import com.lib.base.BaseActivity
import com.lib.tools.LogTool
import com.xiling.play.BuildConfig
import com.xiling.play.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {
    private var mTitle = ""
    private var mUrl = ""
    var mAgentWeb: AgentWeb? = null

    val K_DDM :String = "xl_from"
    val V_DDM :String = BuildConfig.XL_FROM

    private val mUploadMessage: ValueCallback<*>? = null
    private var mUploadCallbackAboveL: ValueCallback<Array<Uri>>? = null
    private val FILECHOOSER_RESULTCODE = 1

    var javaScriptBride: JavaScriptBridge? = null


    override fun getContentViewId(): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw()
        }

        return  R.layout.activity_web_view
    }

    companion object {

        fun jumpUrl(context: Context, url: String?) {
            val lineIt = Intent(context, WebViewActivity::class.java)
            lineIt.putExtra("web_url", url)
            context.startActivity(lineIt)
        }

        fun jumpUrl(
            context: Context,
            title: String?,
            url: String?
        ) {
            val lineIt = Intent(context, WebViewActivity::class.java)
            lineIt.putExtra("web_url", url)
            lineIt.putExtra("web_title", title)
            context.startActivity(lineIt)
        }
    }



    override fun getIntentData(intent: Intent?) {
        mUrl = intent!!.getStringExtra("web_url")
        mTitle = intent!!.getStringExtra("web_title")
    }

    override fun requestData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (StringUtils.isEmpty(mUrl)) {
            ToastUtils.showShortToast("Web访问地址异常")
            finish()
            return
        }

        //返回键拦截到onBackPressed做统一处理
        getHeaderLayout().setOnLeftClickListener(View.OnClickListener { onBackPressed() })

        if (!TextUtils.isEmpty(mTitle)) {
            title = mTitle
        }
        initBridge()
        openWeb(buildUrl(mUrl))
    }


    fun initBridge() {
        javaScriptBride = JavaScriptBridge(this)
    }





    private fun openWeb(url: String?) {
        mAgentWeb = AgentWeb.with(this) //传入Activity
            .setAgentWebParent(
                layoutWebview,
                FrameLayout.LayoutParams(-1, -1)
            ) //传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
            .useDefaultIndicator()
            .setIndicatorColorWithHeight(Color.parseColor("#FF4647"), 0)
            .setWebChromeClient(object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    LogTool.i(TAG,"onConsoleMessage:" + consoleMessage.message())
                    return true
                }

                override fun onShowFileChooser(
                    webView: WebView,
                    filePathCallback: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams
                ): Boolean {
                    mUploadCallbackAboveL = filePathCallback
                    val i = Intent(Intent.ACTION_GET_CONTENT)
                    i.addCategory(Intent.CATEGORY_OPENABLE)
                    i.type = "*/*"
                    this@WebViewActivity.startActivityForResult(
                        Intent.createChooser(
                            i,
                            "File Browser"
                        ), FILECHOOSER_RESULTCODE
                    )
                    return true
                }
            })
            .setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    mUrl = url
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    mUrl = request.url.toString()
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    view.loadUrl("javascript:sessionStorage.setItem('xl_from', '2')")
                    super.onPageFinished(view, url)
                }
            })
            .setReceivedTitleCallback(object : ChromeClientCallbackManager.ReceivedTitleCallback {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    var title = title
                    title = title!!.replace(".html", "")
                    //2018/12/29 需求变更，如果上层传入的title有值则不关心title改变
                    if (TextUtils.isEmpty(mTitle)) {
                        setTitle(title)
                        mTitle = title
                    }
                }
            }) //设置 Web 页面的 title 回调
            .createAgentWeb()
            .ready()
            .go(url)
        //增加WebView
        javaScriptBride!!.setWebView(mAgentWeb!!.getWebCreator().get())
        //增加JSB
        mAgentWeb!!.getJsInterfaceHolder().addJavaObject(JavaScriptBridge.NAME, javaScriptBride)
    }


    fun buildUrl(url: String): String? {
        var url = url
        if (!TextUtils.isEmpty(url)) {
            try {
                val uri = Uri.parse(url)

                //强制拼接平台类型
                if (!uri.queryParameterNames.contains(K_DDM)) {
                    if (!url.contains("?")) {
                        url += "?" + K_DDM + "=" + V_DDM
                    } else {
                        if (url.endsWith("&")) {
                            url += K_DDM + "=" + V_DDM
                        } else {
                            url += "&" + K_DDM + "=" + V_DDM
                        }
                    }
                }

                //强制链接拼接 FUNC
                /*if (!uri.queryParameterNames.contains("func")) {
                    url += "&func=" + BuildConfig.H5_FUNC
                }*/

                //强制拼接邀请码
               /* if (SessionUtil.getInstance().isLogin() && !url.contains("inviteCode")) {
                    url += "&inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode
                }*/


                if (!url.startsWith("http")) {
                    url = "http://$url"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            //URL为空的时候进入空白页
            url = "about:blank"
        }
        LogTool.d(TAG,"buildUrl:$url")
        return url
    }

    override fun onBackPressed() {
        if (!mAgentWeb!!.back()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb!!.destroy()
        }
        super.onDestroy()
    }


}
