package com.xiling.play.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lib.base.BaseFragment
import com.xiling.play.R
import com.xiling.play.activity.OrderListActivit
import com.xiling.play.adapter.UserServiceAdapter
import com.xiling.play.bean.UserServiceBeam
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    lateinit var userManagerAdapter: UserServiceAdapter
    var userManagerList: ArrayList<UserServiceBeam> = ArrayList()


    override fun getContentViewId(): Int {
        return R.layout.fragment_mine
    }

    override fun getIntentData(bundle: Bundle?) {

    }

    override fun requestData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserRecycler()
    }

    private fun initUserRecycler() {
        userManagerAdapter = UserServiceAdapter()
        recyclerUserService.layoutManager = GridLayoutManager(mActivity, 4)
        recyclerUserService.adapter = userManagerAdapter
        userManagerList.clear()
        userManagerList.add(UserServiceBeam(R.drawable.icon_order, "1", "我的订单"))
        userManagerList.add(UserServiceBeam(R.drawable.icon_friends, "2", "推荐好友"))
        userManagerList.add(UserServiceBeam(R.drawable.icon_money, "3.1", "钱包"))
        userManagerList.add(UserServiceBeam(R.drawable.icon_my_collection, "4", "收藏"))

        userManagerAdapter.setNewData(userManagerList)
        userManagerAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (userManagerList.get(position).name) {
                    "我的订单" -> {
                        var intent = Intent(mActivity, OrderListActivit::class.java)
                        mActivity!!.startActivity(intent)
                    }

                    "推荐好友" -> {

                    }

                    "钱包" -> {

                    }

                    "收藏" -> {

                    }

                }

            }


        })

    }

}