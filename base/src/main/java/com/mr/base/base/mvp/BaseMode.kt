package com.lib.base.mvp

abstract class BaseMode<Presenter:BasePresenter<IView>,IView:BaseView>{
    var mPresenter : Presenter? = null
}