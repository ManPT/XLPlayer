package com.lib.base.mvp

import android.os.Bundle
import com.lib.base.BaseActivity
import com.lib.tools.ClassTool

open abstract class BaseMVPActivity<IView : BaseView, Mode : BaseMode<Presenter,IView>, Presenter : BasePresenter<IView>> :
    BaseActivity() {
    var mMode: Mode? = null
    var mPresenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMode = ClassTool.getT<Mode>(this, 1)
        mPresenter = ClassTool.getT<Presenter>(this, 2)
        mPresenter!!.attchView(this as IView)
        mMode!!.mPresenter = mPresenter
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.detachView()
    }

}