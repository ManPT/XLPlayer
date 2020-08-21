package com.lib.base

import androidx.fragment.app.FragmentTransaction


open abstract class BaseFragmentActivity : BaseActivity() {

    /**
     * 获取承载Fragment的View
     *
     * @return
     */
    abstract fun getFragmentViewId(): Int;

    /**
     * 添加fragment
     *
     * @param fragment Fragment
     */
    public fun addFragment(fragment: BaseFragment, addBackStack: Boolean) {
        if (fragment != null) {
            if (getFragmentViewId() > 0) {
                var transaction: FragmentTransaction =
                    getSupportFragmentManager().beginTransaction()
                transaction.replace(getFragmentViewId(), fragment, fragment.javaClass.simpleName)
                if (addBackStack) {
                    transaction.addToBackStack(fragment.javaClass.simpleName)
                }
                transaction.commitAllowingStateLoss()
            }
        }
    }


    public fun removeFragment() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }


    override fun onBackPressed() {
        if (getFragmentViewId() > 0) {
            removeFragment()
        } else {
            super.onBackPressed()
        }
    }

}
