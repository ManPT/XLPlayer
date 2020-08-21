package com.xiling.play.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.tools.ScreenTool
import com.mr.base.base.mvp.BaseMVPFragment
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiling.play.R
import com.xiling.play.adapter.HomeLiveAdapter
import com.xiling.play.adapter.ShopListAdapter
import com.xiling.play.bean.AutoClickBean
import com.xiling.play.bean.IndexHot
import com.xiling.play.bean.IndexHotX
import com.xiling.play.bean.Row
import com.xiling.play.mananger.AutoClickManager
import com.xiling.play.mananger.BannerManager
import com.xiling.play.mananger.GlideManager
import com.xiling.play.mvp.HomeMainMode
import com.xiling.play.mvp.HomeMainPresenter
import com.xiling.play.mvp.HomeMainView
import com.xiling.play.view.divider.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseMVPFragment<HomeMainView, HomeMainMode, HomeMainPresenter>(), HomeMainView,
    OnRefreshListener, OnLoadMoreListener {

    var page: Int = 1

    lateinit var homeLiveAdapter: HomeLiveAdapter
    lateinit var homeShopAdapter: ShopListAdapter


    override fun getContentViewId(): Int {
        return R.layout.fragment_home
    }


    override fun getIntentData(bundle: Bundle?) {


    }

    override fun requestData() {
        mMode!!.requestHomeBanner()
        mMode!!.requestHomeLive()
        requestShop()
    }

    /**
     * 推荐商品请求
     */
    fun requestShop() {
        mMode!!.requestShopList(page)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setOnLoadMoreListener(this)
        smartRefreshLayout.setOnRefreshListener(this)

        recyclerHomeLive.layoutManager =
            LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        homeLiveAdapter = HomeLiveAdapter()
        recyclerHomeLive.addItemDecoration(
            SpacesItemDecoration(
                ScreenTool.dip2px(
                    10f
                ), 0
            )
        )
        recyclerHomeLive.adapter = homeLiveAdapter

        val recommendLayoutManager: GridLayoutManager =
            object : GridLayoutManager(activity, 2) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }

                override fun canScrollVertically(): Boolean {
                    return false
                }

            }

        recyclerHomeShop.layoutManager = recommendLayoutManager
        recyclerHomeShop.addItemDecoration(
            SpacesItemDecoration(
                ScreenTool.dip2px(
                    12f
                ), ScreenTool.dip2px( 12f)
            )
        )
        homeShopAdapter = ShopListAdapter(R.layout.item_home_recommend)
        recyclerHomeShop.adapter = homeShopAdapter
        requestData()
    }

    override fun updateBannerView(imgsList: List<String>) {
        BannerManager.startBanner(banner, imgsList)
    }

    override fun setBannerViewClickListener(autoList: List<AutoClickBean>) {
        banner.setOnBannerListener { position ->
            AutoClickManager.pars(mActivity, autoList.get(position))
        }
    }

    override fun updateLiveRoom(indexHot: IndexHot?) {
        if (indexHot != null) {
            ivLiveRoom.visibility = View.VISIBLE
            GlideManager.loadImage(mActivity, ivLiveRoom, indexHot.imgUrl)
        } else {
            ivLiveRoom.visibility = View.GONE
        }

    }

    override fun updateLiveList(liveList: List<IndexHotX>?) {
        if (liveList != null) {
            homeLiveAdapter.setNewData(liveList)
        } else {
            homeLiveAdapter.setNewData(ArrayList())
        }
    }

    override fun updateShopList(totalPage: Int, rows: List<Row>) {
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()

        if (rows != null) {
            if (page == 1) {
                homeShopAdapter.setNewData(rows)
            } else {
                homeShopAdapter.addData(rows)
            }

            // 如果已经到最后一页了，关闭上拉加载
            if (page >= totalPage) {
                smartRefreshLayout.setEnableLoadMore(false)
            } else {
                smartRefreshLayout.setEnableLoadMore(true)
            }

        }
    }

    override fun error(message: String) {
        super.error(message)
        if (smartRefreshLayout != null){

        }
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        requestShop()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        requestShop()
    }

}
