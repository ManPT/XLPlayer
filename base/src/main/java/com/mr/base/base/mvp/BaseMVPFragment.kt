package com.mr.base.base.mvp

import android.os.Bundle
import android.view.View
import com.lib.base.BaseFragment
import com.lib.base.mvp.BaseMode
import com.lib.base.mvp.BasePresenter
import com.lib.base.mvp.BaseView
import com.lib.tools.ClassTool

abstract class BaseMVPFragment<IView : BaseView, Mode : BaseMode<Presenter, IView>, Presenter : BasePresenter<IView>> :
    BaseFragment() {
    var mMode: Mode? = null
    var mPresenter: Presenter? = null

    open abstract override fun getContentViewId(): Int

    open abstract override fun getIntentData(bundle: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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