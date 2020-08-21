package com.xiling.play.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.base.BaseFragment
import com.mr.base.tools.Tool
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiling.play.R
import com.xiling.play.adapter.MyOrderAdapter
import com.xiling.play.mananger.Constants
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : BaseFragment() ,OnRefreshListener,OnLoadMoreListener{

    private var orderStatus: String = Constants.ORDER_ALL
    var orderAdapter: MyOrderAdapter? = null

    override fun getContentViewId(): Int {
        return R.layout.fragment_order
    }

    override fun getIntentData(bundle: Bundle?) {
        if (bundle != null) {
            orderStatus = arguments!!.getString("order_status")
        }
    }

    override fun requestData() {
        var  list = ArrayList<String>()

        list.add("1")
        list.add("1")
        list.add("1")
        list.add("1")
        smartRefreshLayout.setEnableLoadMore(false)
        orderAdapter!!.setNewData(list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setOnLoadMoreListener(this)
        smartRefreshLayout.setOnRefreshListener(this)

        recyclerOrder.setLayoutManager(LinearLayoutManager(Tool.mContext))
        orderAdapter = MyOrderAdapter()
        recyclerOrder.adapter = orderAdapter

        requestData()
    }

    companion object {
        fun newInstance(orderStatus: String?): OrderFragment {
            val fragment = OrderFragment()
            val args = Bundle()
            args.putString("order_status", orderStatus)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }


}