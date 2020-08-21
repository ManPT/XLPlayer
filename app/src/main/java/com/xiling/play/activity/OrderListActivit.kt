package com.xiling.play.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lib.base.BaseActivity
import com.xiling.play.R
import com.xiling.play.fragment.OrderFragment
import com.xiling.play.mananger.Constants
import kotlinx.android.synthetic.main.activity_order_list.*
import java.util.*

class OrderListActivit : BaseActivity() {

    private val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
    private val childNames: ArrayList<String> = ArrayList()

    fun jumpOrderList(context: Context) {
        val intent = Intent(context, OrderListActivit::class.java)
        context.startActivity(intent)
    }


    override fun getContentViewId(): Int {
        return R.layout.activity_order_list
    }

    override fun getIntentData(intent: Intent?) {

    }

    override fun requestData() {
        childNames.add("全部")
        childNames.add("待付款")
        childNames.add("待发货")
        childNames.add("待收货")
        childNames.add("已完成")

        fragments.add(OrderFragment.newInstance(Constants.ORDER_ALL))
        fragments.add(OrderFragment.newInstance(Constants.ORDER_WAIT_PAY))
        fragments.add(OrderFragment.newInstance(Constants.ORDER_WAIT_SHIP))
        fragments.add(OrderFragment.newInstance(Constants.ORDER_WAIT_RECEIVED))
        fragments.add(OrderFragment.newInstance(Constants.ORDER_IS_RECEIVED))

        slidingTab.setViewPager(
            viewpagerOrder,
            childNames.toTypedArray(),
            this,
            fragments
        )



    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "我的订单"
        setLeftBlack()
        requestData()
    }


}