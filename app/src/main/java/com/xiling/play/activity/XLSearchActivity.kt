package com.xiling.play.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.flexbox.FlexboxLayout
import com.lib.base.BaseActivity
import com.lib.tools.ScreenTool
import com.mr.base.net.RequestListener
import com.mr.base.tools.ToastTool
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiling.play.R
import com.xiling.play.adapter.ShopListAdapter
import com.xiling.play.bean.ShopListBean
import com.xiling.play.http.XLHttpRequest
import com.xiling.play.mananger.XLSearchWordManager
import kotlinx.android.synthetic.main.activity_x_l_search.*
import java.util.*

class XLSearchActivity : BaseActivity(), OnRefreshListener, OnLoadMoreListener {
    private lateinit var shopAdapter: ShopListAdapter
    protected lateinit var mInputMethodManager: InputMethodManager
    private lateinit var searchWordManager: XLSearchWordManager
    private var mKeyword = ""
    private var page = 1

    override fun getContentViewId(): Int {
        return R.layout.activity_x_l_search
    }

    override fun getIntentData(intent: Intent?) {

    }

    override fun requestData() {
        XLHttpRequest.requestShopList(page, "", mKeyword, "", "", object :
            RequestListener<ShopListBean> {
            override fun onStart() {

            }

            override fun onSuccess(result: ShopListBean?) {
                super.onSuccess(result)
                if (result != null) {
                    smartRefreshLayout.finishRefresh()
                    smartRefreshLayout.finishLoadMore()

                    if (result.rows != null) {
                        if (page == 1) {
                            shopAdapter.setNewData(result.rows)
                        } else {
                            shopAdapter.addData(result.rows)
                        }

                        // 如果已经到最后一页了，关闭上拉加载
                        if (page >= result.totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false)
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true)
                        }

                    }
                }
            }

            override fun onError(e: Throwable?) {

            }

            override fun onComplete() {

            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        setLeftBlack()
        setTitle("搜索")
        setStatusBar(true)
        setStatusBarVisable(true)
        searchWordManager = XLSearchWordManager.share()!!
        mInputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setOnLoadMoreListener(this)
        smartRefreshLayout.setOnRefreshListener(this)

        recyclerSearch.layoutManager = LinearLayoutManager(mContext)
        shopAdapter = ShopListAdapter(R.layout.item_shop_category)
        recyclerSearch.adapter = shopAdapter
        loadHotKeywords()
        initEditView()


    }

    private fun initEditView() {
        mKeywordEt.addTextChangedListener(object:TextWatcher{

            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (charSequence != null && charSequence.length > 0) {
                    cleanBtn.setVisibility(View.VISIBLE)
                } else {
                    cleanBtn.setVisibility(View.GONE)
                }
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (charSequence != null && charSequence.length > 0) {
                    cleanBtn.setVisibility(View.VISIBLE)
                } else {
                    cleanBtn.setVisibility(View.GONE)
                   /* loadHotKeywords()
                    keywordsLayout.setVisibility(View.VISIBLE)*/
                }
            }

            override fun afterTextChanged(editable: Editable) {}

        })

        mKeywordEt.setOnEditorActionListener(object : OnEditorActionListener{
            override fun onEditorAction(textView: TextView?, i: Int, keyEvent: KeyEvent?): Boolean {
                if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    val keyword = textView!!.text.toString()
                    search(keyword)
                    return true
                }
                return false
            }

        })

        mKeywordEt.setOnFocusChangeListener(OnFocusChangeListener { view, b ->
            if (b) {
                mInputMethodManager.showSoftInput(
                    mKeywordEt,
                    InputMethodManager.SHOW_IMPLICIT
                )
            } else {
                mInputMethodManager.hideSoftInputFromWindow(mKeywordEt.getWindowToken(), 0)
            }
        })
    }


    private fun loadHotKeywords() {
        val keywords: ArrayList<String> = searchWordManager.getAll()!!
        hotKeywordsLayout.removeAllViews()
        deleteHistoryButton.visibility = if (keywords.size > 0) View.VISIBLE else View.INVISIBLE
        val layoutParams =
            FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenTool.dip2px(30f))
        val margin: Int = ScreenTool.dip2px(5f)
        layoutParams.setMargins(margin, margin, margin, margin)
        val padding: Int = ScreenTool.dip2px(15f)
        for (keyword in keywords) {
            val textView = TextView(mContext)
            textView.layoutParams = layoutParams
            textView.textSize = 12f
            textView.setTextColor(resources.getColor(R.color.default_text_color))
            textView.setBackgroundResource(R.drawable.bg_keyword)
            textView.text = keyword
            textView.setPadding(padding, 0, padding, 0)
            textView.gravity = Gravity.CENTER_VERTICAL
            hotKeywordsLayout.addView(textView)
            textView.setOnClickListener { search(keyword) }
        }

        if (!mKeyword.isEmpty()) {
            keywordsLayout.setVisibility(View.GONE)
        } else {
            keywordsLayout.setVisibility(View.VISIBLE)
            mKeywordEt.requestFocus()
        }
    }


    protected fun search(keyword: String) {
        if (TextUtils.isEmpty(keyword)) {
            ToastTool.showToast("请输入关键词", Toast.LENGTH_SHORT)
            return
        }
        ctrlMode()
        mKeyword = keyword
        mKeywordEt.setText(mKeyword)
        cleanBtn.setVisibility(View.VISIBLE)
        mKeywordEt.clearFocus()
        searchWordManager.addKeyword(keyword)
        requestData()
    }

    fun ctrlMode() {
        recyclerSearch.setVisibility(View.VISIBLE)
        keywordsLayout.setVisibility(View.GONE)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        requestData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        requestData()
    }

    @OnClick(R.id.cleanBtn)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.cleanBtn -> {
                mKeywordEt.requestFocus()
                mKeywordEt.setText("")
                cleanBtn.setVisibility(View.GONE)
            }
        }
    }

}
