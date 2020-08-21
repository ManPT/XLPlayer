package com.xiling.play

import android.app.Application
import android.content.Context
import com.blankj.utilcode.utils.Utils
import com.mr.base.tools.Tool
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.xiling.play.im.IMInterface
import com.xiling.play.im.IMTencentImpl
import com.xiling.play.view.XLRefreshHeader

class XLPlayerApplication : Application() {
    companion object {
        var context: Context? = null
    }

    lateinit var imInterface :IMInterface

    override fun onCreate() {
        super.onCreate()
        context = this
        Utils.init(this)
        Tool.init(this)
        imInterface = IMTencentImpl
        imInterface.init()

        ClassicsFooter.REFRESH_FOOTER_NOTHING = "我也是有底线的"
        //设置全局的Header构建器
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setHeaderHeight(60f)
            XLRefreshHeader(context)
        }
        //设置全局的Footer构建器
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context)
        }

    }


}