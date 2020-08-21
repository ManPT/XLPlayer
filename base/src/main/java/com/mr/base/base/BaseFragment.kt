package com.lib.base


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * BaseFragment
 * 集成fragment的添加与移除
 */
open abstract class BaseFragment : Fragment() {

    protected var mActivity: BaseFragmentActivity? = null
    protected var TAG: String? = ""
    protected var parentView: View? = null;
    /**
     * parentView的Id
     */
    abstract fun getContentViewId(): Int


    /**
     * 获取传值
     */
    abstract fun getIntentData(bundle: Bundle?)

    /**
     * 数据请求
     */
    abstract fun requestData()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseFragmentActivity) {
            mActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = javaClass.simpleName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentView = inflater.inflate(getContentViewId(), container, false)
        return parentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            getIntentData(arguments)
        }
    }

    fun addFragment(fragment: BaseFragment, addBackStack: Boolean) {
        mActivity?.addFragment(fragment, addBackStack)
    }

    fun removeFragment() {
        mActivity?.removeFragment()
    }
}
