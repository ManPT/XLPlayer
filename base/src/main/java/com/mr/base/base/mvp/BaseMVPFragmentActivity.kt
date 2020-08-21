package com.lib.base.mvp

import com.lib.base.BaseActivity
import com.lib.base.BaseFragment
import com.lib.base.BaseFragmentActivity
import com.lib.tools.ClassTool

open abstract class BaseMVPFragmentActivity<IView : BaseView, Mode : BaseMode<Presenter,IView>, Presenter : BasePresenter<IView>> :
    BaseFragmentActivity() {
    var baseMode: Mode? = null
    var mPresenter: Presenter? = null

    override fun requestData() {
        baseMode = ClassTool.getT<Mode>(this, 1)
        mPresenter = ClassTool.getT<Presenter>(this, 2)
        mPresenter!!.attchView(this as IView)
        baseMode!!.mPresenter = mPresenter
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.detachView()
    }

}