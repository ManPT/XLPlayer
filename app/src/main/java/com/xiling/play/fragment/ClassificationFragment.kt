package com.xiling.play.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.mr.base.base.mvp.BaseMVPFragment
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiling.play.R
import com.xiling.play.activity.XLSearchActivity
import com.xiling.play.adapter.CategoryNavigationAdapter
import com.xiling.play.adapter.ShopListAdapter
import com.xiling.play.bean.Row
import com.xiling.play.bean.TopCategoryBean
import com.xiling.play.mvp.ClassificationMode
import com.xiling.play.mvp.ClassificationPresenter
import com.xiling.play.mvp.ClassificationView
import kotlinx.android.synthetic.main.fragment_classification.*

class ClassificationFragment :
    BaseMVPFragment<ClassificationView, ClassificationMode, ClassificationPresenter>(),
    ClassificationView,
    OnRefreshListener, OnLoadMoreListener {
    var unbinder: Unbinder? = null
    var page = 0
    var childPosition: Int = 0
    lateinit var leftAdapter: CategoryNavigationAdapter
    lateinit var rightAdapter: ShopListAdapter


    override fun getContentViewId(): Int {
        return R.layout.fragment_classification
    }

    override fun getIntentData(bundle: Bundle?) {

    }

    override fun requestData() {
        mMode!!.requestTopCategory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)
        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setOnLoadMoreListener(this)
        smartRefreshLayout.setOnRefreshListener(this)

        leftRecyclerView.layoutManager = LinearLayoutManager(mActivity)
        leftAdapter = CategoryNavigationAdapter()
        leftRecyclerView.adapter = leftAdapter

        rightRecyclerView.layoutManager = LinearLayoutManager(mActivity)
        rightAdapter = ShopListAdapter(R.layout.item_shop_category)
        rightRecyclerView.adapter = rightAdapter

        leftAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                childPosition = position
                leftAdapter.setmActiveIndex(childPosition)

                page = 1
                mMode!!.requestSecondCategory(
                    leftAdapter.data.get(childPosition).id.toString(),
                    page
                )
            }

        })

        requestData()
    }

    /**
     *  更新左侧分类列表
     */
    override fun updateCategoryList(categoryList: List<TopCategoryBean>) {
        childPosition = leftAdapter.getmActiveIndex()
        if (categoryList.size > childPosition) {
            leftAdapter.setNewData(categoryList)
            mMode!!.requestSecondCategory(categoryList.get(childPosition).id.toString(), page)
        }

    }

    /**
     * 更新右侧商品列表
     */
    override fun updateSecondCategory(totalPage: Int, rows: List<Row>) {
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()

        if (rows != null) {
            if (page == 1) {
                rightAdapter.setNewData(rows)
            } else {
                rightAdapter.addData(rows)
            }

            // 如果已经到最后一页了，关闭上拉加载
            if (page >= totalPage) {
                smartRefreshLayout.setEnableLoadMore(false)
            } else {
                smartRefreshLayout.setEnableLoadMore(true)
            }

        }


    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mMode!!.requestSecondCategory(leftAdapter.data.get(childPosition).id.toString(), page)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        mMode!!.requestSecondCategory(leftAdapter.data.get(childPosition).id.toString(), page)
    }


    @OnClick(R.id.tv_search)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_search -> {
                startActivity(Intent(mActivity, XLSearchActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }


}
