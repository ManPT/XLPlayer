package com.xiling.play.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lib.base.BaseFragment
import com.lib.base.BaseFragmentActivity
import com.xiling.play.R
import com.xiling.play.adapter.TableLayoutAdapter
import com.xiling.play.bean.TableLayoutBean
import com.xiling.play.fragment.ClassificationFragment
import com.xiling.play.fragment.HomeFragment
import com.xiling.play.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseFragmentActivity() {

    var tableList = ArrayList<TableLayoutBean>()
    var tableAdapter : TableLayoutAdapter? = null
    var fragments = ArrayList<BaseFragment>()

    override fun getFragmentViewId(): Int {
        return R.id.frame_layout
    }

    override fun getContentViewId(): Int {
      return R.layout.activity_main
    }


    override fun getIntentData(intent: Intent?) {

    }

    override fun requestData() {
        fragments.clear()
        fragments.add(HomeFragment())
        fragments.add(ClassificationFragment())
        fragments.add(MineFragment())

        tableList.clear()
        var homeBean:TableLayoutBean = TableLayoutBean("首页",R.drawable.tab_item_home_selector,true)
        tableList.add(homeBean)

        var categoryBean:TableLayoutBean = TableLayoutBean( "分类",R.drawable.tab_item_category_selector,false)
        tableList.add(categoryBean)

        var meBean:TableLayoutBean = TableLayoutBean( "我的",R.drawable.tab_item_me_selector,false)
        tableList.add(meBean)
        if (tableAdapter == null){
            tableAdapter = TableLayoutAdapter()
            tableLayout.adapter = tableAdapter

            var gridLayoutManager: GridLayoutManager = GridLayoutManager(mContext,3)
            tableLayout.layoutManager = gridLayoutManager


        }
        tableAdapter!!.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                   for (i in tableList.indices){
                       tableList.get(i).isEnable = i == position
                   }
                tableAdapter!!.notifyDataSetChanged()
                addFragment(fragments.get(position),false)

            }
        })
       tableAdapter!!.setNewData(tableList)

        addFragment(fragments.get(0),false)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar(true)
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        requestData()
    }

  /*  @OnClick(
        R.id.btnLive,
        R.id.btnPlay
    )
    fun onClick(view : View){
        when(view?.id){
            R.id.btnLive ->{
                startActivity(Intent(this,
                    LiveBroadcastActivity::class.java));
            }

            R.id.btnPlay ->{
                startActivity(Intent(this,
                    IJKPlayerActivity::class.java));
            }

        }

    }*/


}
